(ns purchases-clojure.core
  (:require [clojure.string :as str]
            [clojure.walk :as walk]
            [clojure.pprint :as pprint]
            [ring.adapter.jetty :as jetty]
            [hiccup.core :as h])
  (:gen-class))
(defn read-purchases []
(let [purchases (slurp "purchases.csv")
      purchases (str/split-lines purchases)
      purchases (map (fn [line]
                       (str/split line #","))
                     purchases)
      header (first purchases)
      purchases (rest purchases)
      purchases (map (fn [line]
                       (interleave header line))
                     purchases)
      purchases (map (fn [line]
                       (apply hash-map line))
                     purchases)
      purchases (walk/keywordize-keys purchases)

      _ (println "Type in Catgeory")
      input (read-line)
      category-purchases (filter (fn [line]
                                   (= input (:category line)))
                                 purchases)
      category-purchases (map (fn [line]
                                (assoc line :customer_id_math
                                            (/ (Integer/valueOf (:customer_id line))
                                               2)))
                              category-purchases)
      _(pprint/pprint category-purchases)

      _ (println "Type in CVV")
      input (read-line)
      cvv-purchases (filter (fn [line]
                              (= input (:cvv line)))
                            purchases)
      _(pprint/pprint cvv-purchases)

      _ (println "Type in Credit Card")
      input (read-line)
      credit-card-purchases (filter (fn [line]
                                      (= input (:credit_card line)))
                                    purchases)
      _ (pprint/pprint credit-card-purchases)

      _ (println "Type in Date")
      input (read-line)
      date-purchases (filter (fn [line]
                               (= input (:date line)))         ;comment test
                             purchases)
      _ (pprint/pprint date-purchases)

      _ (println '"Type in Customer Id")
      input (read-line)
      customer-id-purchases (filter (fn [line]
                                      (= input (:customer_id line)))
                                    purchases)
      _ (pprint/pprint customer-id-purchases)]

  ;(pprint/pprint purchases)
  ;(spit (format) "filtered_%s.eden" input)
  ))
(defn purchases-html []
  (let [purchases (read-purchases)]
    (map (fn [line]
           [:p
            (str (:category line)
                 ""
                 (:cvv line)
                 ""
                 (:credit_card line)
                 ""
                 (:date line)
                 ""
                 (:customer_id line))])
         purchases)))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (h/html [:html
                  [:body
                   [:a {:href "http://www.theironyard.com"}
                    "The Iron Yard"]
                   [:br]
                   [:b
                    [:a {:href "http://www.riseofagon.com"}
                     "Rise of Agon"]]
                   [:br]
                   (purchases-html)
                   (:script {:src "path/to/file.s"})]])})

(defn -main [& args]
  (jetty/run-jetty #'handler {:port 3000 :join? false}))