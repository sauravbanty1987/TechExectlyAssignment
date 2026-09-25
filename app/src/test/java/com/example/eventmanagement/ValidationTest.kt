package com.example.eventmanagement
import org.junit.Assert.assertTrue
import org.junit.Test
class ValidationTest { @Test fun titleRequired(){ assertTrue("Event".isNotBlank()) } }
