(ns game-of-life.rules)

(defrecord Cell [pos state])

(defn ->board-of-size [n alive-cells]
  "Creates board of size nxn.
 All positions in `alive-cells` are marked as alive."
  [n]
  (vec
   (for [x (range n)]
     (vec (for [y (range n)]
            (Cell. [x y]
                   (if (some #{[x y]} alive-cells)
                     true
                     false)))))))

(defn map-board
  "Maps "
  [f board]
  (mapv (fn [row]
          (mapv (fn [cell]
                  (f cell))
                row))
        board))

(defn is-valid-position?
  "Checks if position is valid, i.e. within bounds of board."
  [[x y]]
  (and (>= x 0 ) (>= y 0)))

(defn all-possible-neighbours
  "Generates a set of 8 neighbours for position.
Some of generated positions might be invalid, i.e. outside the board."
  [[x y]]
  (for [dx [-1 0 1]
        dy (if (zero? dx) [-1 1] [-1 0 1])]
               [(+ x dx) (+ y dy)]))

(defn neighbours
  "Generates coordinates for neighbours of cell at coordinates."
  [pos]
  (filter is-valid-position? (all-possible-neighbours pos)))

(defn neighbours-alive
  "Returns number of alive neighbours for cell."
  [cell board]
  (reduce (fn [cnt cell] (if (:state cell) (+ cnt 1) cnt))
          0
          (map #(get-in board %) (neighbours (:pos cell)))))

(defn is-alive?
  "Checks if cell is alive."
  [cell  board]
  (let [neighbours-alive (neighbours-alive cell board)]
    (if (:state cell)
      (<= 2 neighbours-alive 3)
      (= neighbours-alive 3))))

(defn step
  "Iterates to the next step in game lifecycle, by updating cell statuses."
  [board]
  (map-board (fn [cell]
               (Cell. (:pos cell)
                      (is-alive? cell board)))
             board))

