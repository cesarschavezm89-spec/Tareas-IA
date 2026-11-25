
(define (domain robot-warehouse)

  (:requirements :strips :typing)

  ;; TIPOS DE OBJETOS
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

    ;; Producto que transporta un robot
    (transportando ?r - robot ?p - producto)

    ;; Robot disponible para recoger productos
    (libre ?r - robot)

    ;; Conexion entre dos ubicaciones
    (conectado ?l1 - ubicacion ?l2 - ubicacion)

    ;; Ubicaciones especiales
    (estacion-empaque ?l - ubicacion)
    (zona-despacho ?l - ubicacion)

    ;; Estado de los productos
    (empaquetado ?p - producto)
    (entregado ?p - producto)
  )

  ;; ACCION 1: MOVER ROBOT
  (:action mover-robot

    :parameters (
      ?r - robot
      ?origen - ubicacion
      ?destino - ubicacion
    )

    :precondition (and
      (robot-en ?r ?origen)
      (conectado ?origen ?destino)
    )

    :effect (and
      (not (robot-en ?r ?origen))
      (robot-en ?r ?destino)
    )
  )

  ;; ACCION 2: RECOGER PRODUCTO
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
    )

    :effect (and
      (not (producto-en ?p ?l))
      (not (libre ?r))
      (transportando ?r ?p)
    )
  )

  ;; ACCION 3: DEJAR PRODUCTO
  (:action dejar-producto

    :parameters (
      ?r - robot
      ?p - producto
      ?l - ubicacion
    )

    :precondition (and
      (robot-en ?r ?l)
      (transportando ?r ?p)
    )

    :effect (and
      (not (transportando ?r ?p))
      (libre ?r)
      (producto-en ?p ?l)
    )
  )

  ;; ACCION 4: EMPAQUETAR PRODUCTO
  (:action empaquetar-producto

    :parameters (
      ?r - robot
      ?p - producto
      ?l - ubicacion
    )

    :precondition (and
      (robot-en ?r ?l)
      (transportando ?r ?p)
      (estacion-empaque ?l)
    )

    :effect (and
      (empaquetado ?p)
    )
  )

  ;; ACCION 5: ENTREGAR PRODUCTO
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
