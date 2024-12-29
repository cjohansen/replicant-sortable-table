(ns boardgames.core
  (:require [boardgames.router :as router]
            [boardgames.ui :as ui]
            [replicant.alias :as alias]
            [replicant.dom :as r]))

(defn routing-anchor [attrs children]
  (into [:a (cond-> attrs
              (:ui/location attrs)
              (assoc :href (router/location->url (:ui/location attrs))))]
        children))

(alias/register! :ui/a routing-anchor)

(defn find-target-href [e]
  (some-> e .-target
          (.closest "a")
          (.getAttribute "href")))

(defn render-location [el state location]
  (r/render el (ui/render-page state location)))

(defn main [el state]
  (->> js/location.pathname
       router/url->location
       (render-location el state)))

(defn get-current-location []
  (->> js/location.pathname
       router/url->location))

(defn route-click [e el state]
  (let [href (find-target-href e)]
    (when-let [location (router/url->location href)]
      (.preventDefault e)
      (if (router/essentially-same? location (get-current-location))
        (.replaceState js/history nil "" href)
        (.pushState js/history nil "" href))
      (render-location el state location))))

(defn bootup [el state]
  (js/document.body.addEventListener "click" #(route-click % el state))

  (js/window.addEventListener
   "popstate"
   (fn [_] (render-location el state (get-current-location))))

  (main el state))
