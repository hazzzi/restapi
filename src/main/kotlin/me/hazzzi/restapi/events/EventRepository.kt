package me.hazzzi.restapi.events

import org.springframework.data.jpa.repository.JpaRepository

interface EventRepository : JpaRepository<Event, Int>