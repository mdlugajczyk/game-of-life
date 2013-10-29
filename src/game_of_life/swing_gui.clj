(ns game-of-life.swing-gui
  (:import javax.swing.JFrame)
  (:import (java.awt Color Dimension))
  (:import (javax.swing JFrame JPanel)))

(def pixel-width 15)
(def pixel-height 15)

(defn- position->pixel [[x y]]
   [(* pixel-width x)
    (* pixel-height y)])

(defn- set-pixel [pos gfx color]
  (let [[pixel-x pixel-y] (position->pixel pos)]
    (.setColor gfx color)
    (.fillRect gfx pixel-x pixel-y pixel-width pixel-height)))

(defn create-frame [board-size panel]
  (let [frame (JFrame. "Game of life")
        [width height] (position->pixel [board-size board-size])]
    (doto frame
      (.add panel)
      (.setSize width height)
      (.setVisible true))
    frame))

(defn- color-for-state [state]
  (if state
    (Color/BLACK)
    (Color/WHITE)))

(defn- render [g board]
  (doall (for [x (range (count @board))
               y (range (count @board))]
           (let [cell (get-in @board [x y])]
             (set-pixel (:pos cell) g (color-for-state (:state cell)))))))

(defn create-panel [board]
    "Create a panel with a customised render"
  (proxy [JPanel] []
    (paintComponent [g]
      (proxy-super paintComponent g)
      (render g board))))



