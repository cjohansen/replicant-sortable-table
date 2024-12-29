(ns boardgames.router
  (:require [lambdaisland.uri :as uri]))

(defn url->location [url]
  (let [uri (cond-> url (string? url) uri/uri)
        query-params (uri/query-map uri)
        hash-params (some-> uri :fragment uri/query-string->map)]
    (cond-> {:location/path (:path uri)}
      (seq query-params) (assoc :location/query-params query-params)
      (seq hash-params) (assoc :location/hash-params hash-params))))

(defn location->url [{:location/keys [path query-params hash-params]}]
  (cond-> path
    (seq query-params)
    (str "?" (uri/map->query-string query-params))

    (seq hash-params)
    (str "#" (uri/map->query-string hash-params))))

(defn essentially-same? [l1 l2]
  (and (= (:location/path l1) (:location/path l2))
       (= (not-empty (:location/params l1))
          (not-empty (:location/params l2)))
       (= (not-empty (:location/query-params l1))
          (not-empty (:location/query-params l2)))))
