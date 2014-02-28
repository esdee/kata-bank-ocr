(ns kata-bank-ocr.core
  (require [clojure.java.io :as io]
           [clojure.string :as str]))

(def number-map
  {" _ | ||_|" 0
   "     |  |" 1
   " _  _||_ " 2
   " _  _| _|" 3
   "   |_|  |" 4
   " _ |_  _|" 5
   " _ |_ |_|" 6
   " _   |  |" 7
   " _ |_||_|" 8
   " _ |_| _|" 9})

(defn file->account-lines
  "Parse a account number file to a seq of account-number-lines.
   Each account number line is a seq of 3 27 character strings"
  [file-location]
  (with-open [rdr (io/reader file-location)]
    (doall (partition 3 4 (line-seq rdr)))))

(defn account-lines->account-number
  "Parse an account line which is a seq consisting of 3 27 character strings and
    return a seq of 9 digits that mae up the account number.
  Illegible characters are returned as nil."
  [line]
  (let [characters (map (fn [index]
                          (let [next3 #(-> % vec (subvec index (+ 3 index)))
                                substr #(apply str (next3 %))] ;; [\| \_ \|] => "|_|"
                            (apply str (map substr line))))
                        ;; (0 3 6 ... 24)
                        (take-nth 3 (range 27)))]
    (map number-map characters)))

(defn valid?
  "Returns true if the account number is valid"
  [account-number]
  (let [checksum (->> (reverse account-number)
                      (map-indexed #(* (inc %1) %2))
                      (apply +))]
    (zero? (mod checksum 11))))

(defn illegible?
  [account-number]
  "Returns true if any character in the account number could not be read"
  (true? (some nil? account-number)))

(defn parse-file
  "Given an account number file will return a seq of lines 
   consisting of the parsed account number and the status"
  [file-location]
  (let [->str (fn [acn & status]
                (let [s (apply str (map #(str (or % "?")) acn))]
                  (str/trim (str s " " (first status)))))]
    (->> (file->account-lines file-location)
         (map account-lines->account-number)
         (map (fn [acn]
                (cond
                 (illegible? acn) (->str acn "ILL")
                 (valid? acn)     (->str acn)
                 :else            (->str acn "ERR")))))))




