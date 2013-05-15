(ns integration.loader-test
  "The 'integration_test' dataset is designed to simplify testing.
  Its data values are simple and map to the row number.

  The tests below are to ensure data is loaded correctly."
  (:require [midje.sweet :refer :all]
            [cfpb.qu.data :as data]
            [cfpb.qu.loader :as loader]))

(def db "integration_test")
(def coll "incomes")

(with-state-changes [(before :facts (do
                                      (data/connect-mongo)
                                      (loader/load-dataset db)))
                     (after :facts (data/disconnect-mongo))]

  (facts "about dates"
         (fact "stored as dates in MongoDB"
               (let [query {:query {} :fields {} :limit 100 :skip 0 :sort {}}
                     result (data/get-find db coll query)]
                 (doseq [datum (:data result)]
                   (class (:date_observed datum)) =>
                   org.joda.time.DateTime)))))
