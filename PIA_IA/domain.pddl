(define (domain robot-warehouse)

  (:requirements :strips :typing)

  ;; TIPOS
  (:types
    robot
    producto
    ubicacion
  )

  ;; PREDICADOS
  (:predicates

    ;; Ubicacion de los robots
    (robot-en ?r - robot ?l - ubicacion)

    ;; Ubicacion de los productos
    (producto-en ?p - producto ?l - ubicacion)

    ;; Producto transportado
    (transportando ?r - robot ?p - producto)

    ;; Disponibilidad del robot
    (libre ?r - robot)

    ;; Conexiones entre ubicaciones
    (conectado ?l1 - ubicacion ?l2 - ubicacion)

    ;; Estaciones especiales
    (estacion-empaque ?l - ubicacion)
    (zona-despacho ?l - ubicacion)
    (estacion-carga ?l - ubicacion)

    ;; Estado de los productos
    (pendiente-empaque ?p - producto)
    (empaquetado ?p - producto)
    (entregado ?p - producto)

    ;; Estados de bateria
    (bateria-alta ?r - robot)
    (bateria-media ?r - robot)
    (bateria-baja ?r - robot)
    (bateria-agotada ?r - robot)
  )

  ;; ACCION 1: MOVER CON BATERIA ALTA
  (:action mover-robot-alta

    :parameters (
      ?r - robot
      ?origen - ubicacion
      ?destino - ubicacion
    )

    :precondition (and
      (robot-en ?r ?origen)
      (conectado ?origen ?destino)
      (bateria-alta ?r)
    )

    :effect (and
      (not (robot-en ?r ?origen))
      (robot-en ?r ?destino)
      (not (bateria-alta ?r))
      (bateria-media ?r)
    )
  )

  ;; ACCION 2: MOVER CON BATERIA MEDIA
  (:action mover-robot-media

    :parameters (
      ?r - robot
      ?origen - ubicacion
      ?destino - ubicacion
    )

    :precondition (and
      (robot-en ?r ?origen)
      (conectado ?origen ?destino)
      (bateria-media ?r)
    )

    :effect (and
      (not (robot-en ?r ?origen))
      (robot-en ?r ?destino)
      (not (bateria-media ?r))
      (bateria-baja ?r)
    )
  )

  ;; ACCION 3: MOVER CON BATERIA BAJA
  (:action mover-robot-baja

    :parameters (
      ?r - robot
      ?origen - ubicacion
      ?destino - ubicacion
    )

    :precondition (and
      (robot-en ?r ?origen)
      (conectado ?origen ?destino)
      (bateria-baja ?r)
    )

    :effect (and
      (not (robot-en ?r ?origen))
      (robot-en ?r ?destino)
      (not (bateria-baja ?r))
      (bateria-agotada ?r)
    )
  )

  ;; ACCION 4: RECARGAR DESDE BATERIA MEDIA
  (:action recargar-media

    :parameters (
      ?r - robot
      ?l - ubicacion
    )

    :precondition (and
      (robot-en ?r ?l)
      (estacion-carga ?l)
      (bateria-media ?r)
    )

    :effect (and
      (not (bateria-media ?r))
      (bateria-alta ?r)
    )
  )

  ;; ACCION 5: RECARGAR DESDE BATERIA BAJA
  (:action recargar-baja

    :parameters (
      ?r - robot
      ?l - ubicacion
    )

    :precondition (and
      (robot-en ?r ?l)
      (estacion-carga ?l)
      (bateria-baja ?r)
    )

    :effect (and
      (not (bateria-baja ?r))
      (bateria-alta ?r)
    )
  )

  ;; ACCION 6: RECARGAR DESDE BATERIA AGOTADA
  (:action recargar-agotada

    :parameters (
      ?r - robot
      ?l - ubicacion
    )

    :precondition (and
      (robot-en ?r ?l)
      (estacion-carga ?l)
      (bateria-agotada ?r)
    )

    :effect (and
      (not (bateria-agotada ?r))
      (bateria-alta ?r)
    )
  )

  ;; ACCION 7: RECOGER PRODUCTO
  (:action recoger-producto

    :parameters (
      ?r - robot
      ?p - producto
      ?l - ubicacion
    )

    :precondition (and
      (robot-en ?r ?l)
      (producto-en ?p ?l)
      (libre ?r)
      (pendiente-empaque ?p)
    )

    :effect (and
      (not (producto-en ?p ?l))
      (not (libre ?r))
      (transportando ?r ?p)
    )
  )

  ;; ACCION 8: DEJAR PRODUCTO
  (:action dejar-producto

    :parameters (
      ?r - robot
      ?p - producto
      ?l - ubicacion
    )

    :precondition (and
      (robot-en ?r ?l)
      (transportando ?r ?p)
      (pendiente-empaque ?p)
    )

    :effect (and
      (not (transportando ?r ?p))
      (libre ?r)
      (producto-en ?p ?l)
    )
  )

  ;; ACCION 9: EMPAQUETAR PRODUCTO
  (:action empaquetar-producto

    :parameters (
      ?r - robot
      ?p - producto
      ?l - ubicacion
    )

    :precondition (and
      (robot-en ?r ?l)
      (transportando ?r ?p)
      (pendiente-empaque ?p)
      (estacion-empaque ?l)
    )

    :effect (and
      (not (pendiente-empaque ?p))
      (empaquetado ?p)
    )
  )

  ;; ACCION 10: ENTREGAR PRODUCTO
  (:action entregar-producto

    :parameters (
      ?r - robot
      ?p - producto
      ?l - ubicacion
    )

    :precondition (and
      (robot-en ?r ?l)
      (transportando ?r ?p)
      (empaquetado ?p)
      (zona-despacho ?l)
    )

    :effect (and
      (not (transportando ?r ?p))
      (not (empaquetado ?p))
      (libre ?r)
      (entregado ?p)
    )
  )
)