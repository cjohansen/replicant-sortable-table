(ns boardgames.ui
  (:require [boardgames.ui.sortable-table :as st]))

(def columns
  [{:f :boardgame/title, :id "title", :label "Title"}
   {:f :boardgame/release-year, :id "year", :label "Released"}
   {:f :bgg/ranking, :id "ranking", :label "Ranking", :default? true}
   {:f :bgg/geek-rating, :id "rating", :label "Geek rating"}
   {:f :bgg/average-rating, :id "average", :label "Avg. rating"}
   {:f :bgg/num-voters, :id "voters", :label "Voters"}])

(defn render-page [{:keys [boardgames]} location]
  [:div.p-8.max-w-screen-lg
   [:h1.text-2xl.font-serif.mb-4 "Boardgames ranked by Boardgamegeek"]
   [::st/table.w-full
    {::st/location location
     ::st/columns columns}
    [::st/thead.border-b.border-gray-200.bg-base-200
     [::st/th.py-2.text-left.px-4]
     [::st/th.py-2.text-left.pr-4]
     [::st/th.py-2.text-left.pr-4]
     [::st/th.py-2.whitespace-nowrap.text-left.pr-4]
     [::st/th.py-2.whitespace-nowrap.text-left.pr-4]
     [::st/th.py-2.text-right.px-4]]
    [::st/tbody.border-b.border-1.border-gray-200
     {::st/data boardgames}
     [::st/th.py-2.px-4.text-left]
     [::st/td.py-2.pr-4.text-left]
     [::st/td.py-2.pr-4.text-center]
     [::st/td.py-2.pr-4.text-left]
     [::st/td.py-2.pr-4.text-left]
     [::st/td.py-2.px-4.text-right]]]])
