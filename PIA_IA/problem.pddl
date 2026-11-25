(define (problem problema-almacen)

  (:domain robot-warehouse)

  ;; OBJETOS
  (:objects

    robot1 robot2 - robot

    producto1 producto2 producto3 - producto

    zona-a zona-b zona-c
    pasillo-central empaque despacho - ubicacion
  )

  ;; ESTADO INICIAL
  (:init

    ;; Posiciones iniciales de los robots
    (robot-en robot1 zona-a)
    (robot-en robot2 zona-b)

    ;; Posiciones iniciales de los productos
    (producto-en producto1 zona-a)
    (producto-en producto2 zona-b)
    (producto-en producto3 zona-c)

    ;; Todos los productos pendientes de empaque
    (pendiente-empaque producto1)
    (pendiente-empaque producto2)
    (pendiente-empaque producto3)

    ;; Los robots comienzan libres
    (libre robot1)
    (libre robot2)

    ;; Baterias iniciales
    (bateria-alta robot1)
    (bateria-alta robot2)

    ;; Estaciones especiales
    (estacion-empaque empaque)
    (zona-despacho despacho)

    ;; Estaciones de carga
    (estacion-carga pasillo-central)
    (estacion-carga despacho)

    ;; CONEXIONES ENTRE UBICACIONES

    ;; Zona A y pasillo central
    (conectado zona-a pasillo-central)
    (conectado pasillo-central zona-a)

    ;; Zona B y pasillo central
    (conectado zona-b pasillo-central)
    (conectado pasillo-central zona-b)

    ;; Zona C y pasillo central
    (conectado zona-c pasillo-central)
    (conectado pasillo-central zona-c)

    ;; Pasillo central y empaque
    (conectado pasillo-central empaque)
    (conectado empaque pasillo-central)

    ;; Empaque y despacho
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