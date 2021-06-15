'use strict'
var domQuery = require('min-dom').query

var entryFieldDescription = require('./EntryFieldDescription')

var textField = function(options, defaultParameters) {
  // Default action for the button next to the input-field
  var defaultButtonAction = function(element, inputNode) {
    var input = domQuery('input[name="' + options.modelProperty + '"]', inputNode)
    input.value = ''

    return true
  }

  // default method to determine if the button should be visible
  var defaultButtonShow = function(element, inputNode) {
    var input = domQuery('input[name="' + options.modelProperty + '"]', inputNode)

    return input.value !== ''
  }

  var resource = defaultParameters
  var label = options.label || resource.id
  var dataValueLabel = options.dataValueLabel
  var buttonLabel = (options.buttonLabel || 'X')
  var actionName = (typeof options.buttonAction !== 'undefined') ? options.buttonAction.name : 'clear'
  var actionMethod = (typeof options.buttonAction !== 'undefined') ? options.buttonAction.method : defaultButtonAction
  var showName = (typeof options.buttonShow !== 'undefined') ? options.buttonShow.name : 'canClear'
  var showMethod = (typeof options.buttonShow !== 'undefined') ? options.buttonShow.method : defaultButtonShow
  var canBeDisabled = !!options.disabled && typeof options.disabled === 'function'
  var canBeHidden = !!options.hidden && typeof options.hidden === 'function'
  var description = options.description

  resource.html =
    '<label for="camunda-' + resource.id + '" ' +
      (canBeDisabled ? 'data-disable="isDisabled" ' : '') +
      (canBeHidden ? 'data-show="isHidden" ' : '') +
      (dataValueLabel ? 'data-value="' + dataValueLabel + '"' : '') + '>' + label + '</label>' +
    '<div class="bpp-field-wrapper" ' +
      (canBeDisabled ? 'data-disable="isDisabled"' : '') +
      (canBeHidden ? 'data-show="isHidden"' : '') +
      '>' +
      '<input id="camunda-' + resource.id + '" type="text" name="' + options.modelProperty + '" ' +
        (canBeDisabled ? 'data-disable="isDisabled"' : '') +
        (canBeHidden ? 'data-show="isHidden"' : '') +
        ' />' +
      '<button class="' + actionName + '" data-action="' + actionName + '" data-show="' + showName + '" ' +
        (canBeDisabled ? 'data-disable="isDisabled"' : '') +
        (canBeHidden ? ' data-show="isHidden"' : '') + '>' +
        '<span>' + buttonLabel + '</span>' +
      '</button>' +
    '</div>'
  // add description below text input entry field
  if (description) {
    resource.html += entryFieldDescription(description)
  }

  resource[actionName] = actionMethod
  resource[showName] = showMethod

  if (canBeDisabled) {
    resource.isDisabled = function() {
      return options.disabled.apply(resource, arguments)
    }
  }

  if (canBeHidden) {
    resource.isHidden = function() {
      return !options.hidden.apply(resource, arguments)
    }
  }

  resource.cssClasses = ['bpp-textfield']

  return resource
}

module.exports = textField
