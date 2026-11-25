
(define (problem problema-almacen)

  (:domain robot-warehouse)

  ;; OBJETOS DEL ESCENARIO
  (:objects

    robot1 robot2 - robot

    producto1 producto2 producto3 - producto

    zona-a zona-b empaque despacho - ubicacion
  )

  ;; ESTADO INICIAL
  (:init

    ;; Posicion de los robots
    (robot-en robot1 zona-a)
    (robot-en robot2 zona-b)

    ;; Posicion de los productos
    (producto-en producto1 zona-a)
    (producto-en producto2 zona-b)
    (producto-en producto3 zona-a)

    ;; Ambos robots comienzan libres
    (libre robot1)
    (libre robot2)

    ;; Estaciones especiales
    (estacion-empaque empaque)
    (zona-despacho despacho)

    ;; Conexiones entre ubicaciones
    (conectado zona-a empaque)
    (conectado empaque zona-a)

    (conectado zona-b empaque)
    (conectado empaque zona-b)

    (conectado empaque despacho)
    (conectado despacho empaque)
  )

  ;; OBJETIVO
  (:goal
    (and
      (entregado producto1)
      (entregado producto2)
      (entregado producto3)
    )
  )
)
