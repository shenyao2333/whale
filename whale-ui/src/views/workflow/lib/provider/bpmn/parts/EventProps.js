'use strict'

var is = require('bpmn-js/lib/util/ModelUtil').is
var isAny = require('bpmn-js/lib/features/modeling/util/ModelingUtil').isAny
var getBusinessObject = require('bpmn-js/lib/util/ModelUtil').getBusinessObject
var eventDefinitionHelper = require('../../../helper/EventDefinitionHelper')

var forEach = require('lodash/forEach')

var message = require('./implementation/MessageEventDefinition')
var signal = require('./implementation/SignalEventDefinition')
var error = require('./implementation/ErrorEventDefinition')
var escalation = require('./implementation/EscalationEventDefinition')
var timer = require('./implementation/TimerEventDefinition')
var compensation = require('./implementation/CompensateEventDefinition')
var condition = require('./implementation/ConditionalEventDefinition')

module.exports = function(group, element, bpmnFactory, elementRegistry, translate) {
  var events = [
    'bpmn:StartEvent',
    'bpmn:EndEvent',
    'bpmn:IntermediateThrowEvent',
    'bpmn:BoundaryEvent',
    'bpmn:IntermediateCatchEvent'
  ]

  // Message and Signal Event Definition
  forEach(events, function(event) {
    if (is(element, event)) {
      var messageEventDefinition = eventDefinitionHelper.getMessageEventDefinition(element)
      var signalEventDefinition = eventDefinitionHelper.getSignalEventDefinition(element)

      if (messageEventDefinition) {
        message(group, element, bpmnFactory, messageEventDefinition, translate)
      }

      if (signalEventDefinition) {
        signal(group, element, bpmnFactory, signalEventDefinition, translate)
      }
    }
  })

  // Special Case: Receive Task
  if (is(element, 'bpmn:ReceiveTask')) {
    message(group, element, bpmnFactory, getBusinessObject(element), translate)
  }

  // Error Event Definition
  var errorEvents = [
    'bpmn:StartEvent',
    'bpmn:BoundaryEvent',
    'bpmn:EndEvent'
  ]

  forEach(errorEvents, function(event) {
    if (is(element, event)) {
      var errorEventDefinition = eventDefinitionHelper.getErrorEventDefinition(element)

      if (errorEventDefinition) {
        var isCatchingErrorEvent = is(element, 'bpmn:StartEvent') || is(element, 'bpmn:BoundaryEvent')

        var showErrorCodeVariable = isCatchingErrorEvent
        var showErrorMessageVariable = isCatchingErrorEvent

        error(group, element, bpmnFactory, errorEventDefinition, showErrorCodeVariable, showErrorMessageVariable,
          translate)
      }
    }
  })

  // Escalation Event Definition
  var escalationEvents = [
    'bpmn:StartEvent',
    'bpmn:BoundaryEvent',
    'bpmn:IntermediateThrowEvent',
    'bpmn:EndEvent'
  ]

  forEach(escalationEvents, function(event) {
    if (is(element, event)) {
      var showEscalationCodeVariable = is(element, 'bpmn:StartEvent') || is(element, 'bpmn:BoundaryEvent')

      // get business object
      var escalationEventDefinition = eventDefinitionHelper.getEscalationEventDefinition(element)

      if (escalationEventDefinition) {
        escalation(group, element, bpmnFactory, escalationEventDefinition, showEscalationCodeVariable,
          translate)
      }
    }
  })

  // Timer Event Definition
  var timerEvents = [
    'bpmn:StartEvent',
    'bpmn:BoundaryEvent',
    'bpmn:IntermediateCatchEvent'
  ]

  forEach(timerEvents, function(event) {
    if (is(element, event)) {
      // get business object
      var timerEventDefinition = eventDefinitionHelper.getTimerEventDefinition(element)

      if (timerEventDefinition) {
        timer(group, element, bpmnFactory, timerEventDefinition, translate)
      }
    }
  })

  // Compensate Event Definition
  var compensationEvents = [
    'bpmn:EndEvent',
    'bpmn:IntermediateThrowEvent'
  ]

  forEach(compensationEvents, function(event) {
    if (is(element, event)) {
      // get business object
      var compensateEventDefinition = eventDefinitionHelper.getCompensateEventDefinition(element)

      if (compensateEventDefinition) {
        compensation(group, element, bpmnFactory, compensateEventDefinition, elementRegistry, translate)
      }
    }
  })

  // Conditional Event Definition
  var conditionalEvents = [
    'bpmn:StartEvent',
    'bpmn:BoundaryEvent',
    'bpmn:IntermediateThrowEvent',
    'bpmn:IntermediateCatchEvent'
  ]

  if (isAny(element, conditionalEvents)) {
    // get business object
    var conditionalEventDefinition = eventDefinitionHelper.getConditionalEventDefinition(element)

    if (conditionalEventDefinition) {
      condition(group, element, bpmnFactory, conditionalEventDefinition, elementRegistry, translate)
    }
  }
}
