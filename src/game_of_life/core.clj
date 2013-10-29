(ns game-of-life.core
    (:use [game-of-life.swing-gui]
          [game-of-life.rules]))


(def glider [[5 5] [6 5] [7 5] [7 4] [6 3]])

(defn run-game [board update-fun]
  (let [panel (create-panel board)
        frame (create-frame (count @board) panel)]
    (loop []
      (.repaint panel)
      (update-fun)
      (Thread/sleep 300)
      (recur))))

(defn -main
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (let [board-size 50
        board (atom (->board-of-size board-size glider))]
    (run-game board
              (fn [] (swap! board step)))))
