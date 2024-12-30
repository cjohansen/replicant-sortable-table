(ns boardgames.ui.sortable-table
  (:require [replicant.alias :refer [defalias]]
            [replicant.hiccup :as hiccup]))

(def reverse-order
  {"desc" "asc"
   "asc" "desc"})

(def comparators
  {"desc" >
   "asc" <})

(defn get-sort-order [location]
  (if (= "desc" (-> location :location/hash-params :sort-order))
    "desc"
    "asc"))

(defn get-sort-column [location columns]
  (let [param (-> location :location/hash-params :sort-column)]
    (or (first (filter (comp #{param} :id) columns))
        (first (filter :default? columns))
        (first columns))))

(defalias table [attrs children]
  (into
   [:table attrs]
   (mapv #(hiccup/update-attrs
           % assoc
           ::location (::location attrs)
           ::columns (::columns attrs)
           ::sort-order (get-sort-order (::location attrs))
           ::sort-column (get-sort-column (::location attrs) (::columns attrs)))
         children)))

(defalias thead [attrs children]
  [:thead
   (into
    [:tr attrs]
    (map-indexed
     (fn [idx child]
       (hiccup/update-attrs
        child assoc
        ::location (::location attrs)
        ::column (nth (::columns attrs) idx)
        ::sort-order (get-sort-order (::location attrs))
        ::sort-column (get-sort-column (::location attrs) (::columns attrs))))
     children))])

(defalias tbody [{::keys [columns sort-column sort-order data] :as attrs} children]
  (into
   [:tbody]
   (->> data
        (sort-by (:f sort-column) (comparators sort-order))
        (mapv
         (fn [cell-data]
           (into [:tr (assoc attrs :replicant/key cell-data)]
                 (map-indexed
                  (fn [col-idx cell]
                    (hiccup/update-attrs
                     cell assoc
                     ::column (nth columns col-idx)
                     ::data cell-data))
                  children)))))))

(defalias th [{::keys [column sort-column sort-order location] :as attrs}]
  (if (::data attrs)
    [:th attrs ((:f column) (::data attrs))]
    [:th attrs
     [:ui/a
      {:ui/location
       (if (= (:id column) (:id sort-column))
         (assoc-in location [:location/hash-params :sort-order]
                   (reverse-order sort-order))
         (assoc-in location [:location/hash-params :sort-column]
                   (:id column)))}
      (when (= (:id column) (:id sort-column))
        (if (= "desc" sort-order) "▼ " "▲ "))
      (:label column)]]))

(defalias td [{::keys [column data] :as attrs}]
  [:td attrs ((:f column) data)])
