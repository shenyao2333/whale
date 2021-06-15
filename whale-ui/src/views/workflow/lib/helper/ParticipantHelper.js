'use strict'

var is = require('bpmn-js/lib/util/ModelUtil').is
var getBusinessObject = require('bpmn-js/lib/util/ModelUtil').getBusinessObject
var cmdHelper = require('./CmdHelper')

var ParticipantHelper = {}

module.exports = ParticipantHelper

ParticipantHelper.modifyProcessBusinessObject = function(element, property, values) {
  if (!is(element, 'bpmn:Participant')) {
    return {}
  }

  var bo = getBusinessObject(element).get('processRef')
  var properties = {}

  properties[property] = values[property]

  return cmdHelper.updateBusinessObject(element, bo, properties)
}

ParticipantHelper.getProcessBusinessObject = function(element, propertyName) {
  if (!is(element, 'bpmn:Participant')) {
    return {}
  }

  var bo = getBusinessObject(element).get('processRef')
  var properties = {}

  properties[propertyName] = bo.get(propertyName)

  return properties
}
