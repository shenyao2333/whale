'use strict'

var getBusinessObject = require('bpmn-js/lib/util/ModelUtil').getBusinessObject
var is = require('bpmn-js/lib/util/ModelUtil').is
var asyncContinuation = require('./implementation/AsyncContinuation')

module.exports = function(group, element, bpmnFactory, translate) {
  if (is(element, 'camunda:AsyncCapable')) {
    group.entries = group.entries.concat(asyncContinuation(element, bpmnFactory, {
      getBusinessObject: getBusinessObject
    }, translate))
  }
}
