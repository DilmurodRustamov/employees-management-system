/*<![CDATA[*/
if (window.matchMedia('(max-width: 1366px)').matches)
  $('.forced').addClass('enlarged');

let labels = ['inverse', 'primary', 'success', 'default', 'purple', 'danger', 'info', 'warning', 'pink'];
let stateLabels = new Map([['inactive', 'inverse'], ['active', 'success'], ['blocked', 'danger'], ['new', 'info'], ['closed', 'inverse']]);
let terminalLabels = new Map([['none', 'default'], ['unauthorized', 'inverse'], ['registration', 'info'], ['active', 'success'], ['modified', 'warning'], ['blocked', 'danger']]);

$.urlParam = function (name, href) {
  let url = typeof href === 'undefined' ? window.location.href : href;

  let params = url.split('&');
  let results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(url);
  let result = results != null ? (decodeURI(results[1]) || 0) : null;
  if (params.length > 1)
    result += "?" + params.slice(1).join("&");
  return result;
};

function contentReload(event, element) {
  event.preventDefault();
  let link = element.attr('href');
  let url = $.urlParam('page', link);
  $.ajax({
    url: url,
    success: function (response) {
      $('.content').html(response);
      window.history.pushState(null, null, link);
    }
  });
  $("#sidebar-menu a.active").removeClass('active');
}

function callBlockDialog(event, url) {
  event.preventDefault();
  let open = $(this).find('span').hasClass('md-lock-open');
  swal({
    title: $.i18n.prop((open ? 'un' : '') + 'lock') + '?',
    type: "warning",
    showCancelButton: true,
    confirmButtonText: $.i18n.prop('yes'),
    confirmButtonClass: 'btn-danger m-l-10',
    cancelButtonText: $.i18n.prop('canc'),
    closeOnConfirm: false
  }, function () {
    $.ajax({
      url: url, type: 'PUT',
      beforeSend: beforeSend,
      success: function (blocked) {
        if (blocked) {
          swal($.i18n.prop('success'), $.i18n.prop('row_changed'), "success");
          $('#datatable').DataTable().ajax.reload();
        } else {
          swal($.i18n.prop('error'), $.i18n.prop('cant_block'), "error");
        }
      },
      error: function () {
        swal($.i18n.prop('error'), '', "error");
      }
    })
  });
}

function deleteRow(url) {
  $.ajax({
    url: url,
    type: 'DELETE',
    beforeSend: beforeSend,
    success: function (deleted) {
      if (deleted) {
        swal($.i18n.prop('row_deleted'), '', "success");
        let table = $('#datatable');
        if (table.length)
          table.DataTable().ajax.reload();
        else
          location.reload();
      } else {
        swal($.i18n.prop('error'), $.i18n.prop('cant_delete_row'), "error");
      }
    },
    error: function () {
      swal($.i18n.prop('error'), $.i18n.prop('cant_delete_row'), "error");
    }
  });
}

function callDeleteDialog(event, url) {
  event.preventDefault();
  swal({
    title: $.i18n.prop('are_you_sure_delete_row'),
    type: "error",
    showCancelButton: true,
    confirmButtonText: $.i18n.prop('del'),
    confirmButtonClass: 'btn-danger m-l-10',
    cancelButtonText: $.i18n.prop('canc'),
    closeOnConfirm: false
  }, function () {
    deleteRow(url)
  });
}

function ajaxError(jqXHR, textStatus, errorThrown) {
  alert(jqXHR + ' ' + textStatus + ' ' + errorThrown);
  console.log(jqXHR, textStatus, errorThrown);
}

function bufferToHex(buffer) {
  return [...new Uint8Array(buffer)]
    .map(b => b.toString(16).padStart(2, "0"))
    .join("");
}

function beforeSend(xhr, settings) {
  let token = $("meta[name='_csrf']").attr("content");
  let header = $("meta[name='_csrf_header']").attr("content");
  if (!/^(GET|HEAD|OPTIONS|TRACE)$/i.test(settings.type)) {
    xhr.setRequestHeader(header, token);
  }
}

function readURL(input) {
  if (input.files && input.files[0]) {
    if (!input.files[0].name.match(/.(png)$/i)) {
      swal($.i18n.prop('error'), $.i18n.prop('logo_upload_hint'), 'error');
      input.files = null;
      $('.modal').modal('hide');
      return;
    }
    var reader = new FileReader();
    reader.onload = function (e) {
      var image = new Image();
      image.src = e.target.result;
      image.onload = function () {
        var height = this.height;
        var width = this.width;
        if (width == 200 && height == 200) {
          $('#img-preview').attr('src', e.target.result);
          result = true;
        } else {
          swal($.i18n.prop('error'),
            $.i18n.prop('selected_image_size') + ' ' + width + 'px x ' + height + 'px\n' + $.i18n.prop('logo_upload_hint'),
            'error');
          $('.modal').modal('hide');
        }
      }
    };
    reader.readAsDataURL(input.files[0]); // convert to base64 string
  }
}

function formatDate(date) {
  let d = new Date(date),
    month = '' + (d.getMonth() + 1),
    day = '' + d.getDate(),
    year = d.getFullYear() % 2000;

  if (month.length < 2) month = '0' + month;
  if (day.length < 2) day = '0' + day;

  return [day, month, year].join('.');
}

function formatTime(date) {
  let d = new Date(date),
    hours = '' + d.getHours(),
    minutes = '' + d.getMinutes(),
    seconds = '' + d.getSeconds();
  if (hours.length < 2) hours = '0' + hours;
  if (minutes.length < 2) minutes = '0' + minutes;
  if (seconds.length < 2) seconds = '0' + seconds;
  return [hours, minutes, seconds].join(':');
}

function formatTimeHHmm(date) {
  let d = new Date(date),
    hours = '' + d.getHours(),
    minutes = '' + d.getMinutes();
  if (hours.length < 2) hours = '0' + hours;
  if (minutes.length < 2) minutes = '0' + minutes;
  return [hours, minutes].join(':');
}

function formatDatetime(date, notBreak) {
  let time = formatTimeHHmm(date);
  date = formatDate(date);
  return notBreak ? time + " " + date : date + "<br/> " + time;
}

function getExpiracyLabel(data, days) {
  if (data == null
    || (new Date().getTime() - new Date(data).getTime()) / (1000 * 60 * 60 * 24) > days)
    return "danger";
  return "default";
}

function formatPan(pan) {
  let num = pan.match(/.{1,4}/g);
  return num.join(' ');
}

function formatExpireDate(expDate) {
  return expDate.substring(2, 4) + '/' + expDate.substring(0, 2);
}

function formatBdd(data) {
  if (data < 200000)
    return "";
  let day = data % 100;
  if (day < 10) day = '0' + day;
  let month = Math.round(data / 100 % 100);
  if (month < 10) month = '0' + month;
  return day + '.' + month + '.' + Math.round(data / 10000);
}

function getTrxLabelColor(arr, e) {
  switch (arr.indexOf(e)) {
    case 0:
      return 'warning';
    case 1:
      return 'success';
    case 2:
      return 'inverse';
    case 3:
    case 4:
    case 5:
    case 6:
      return 'danger';
    case 7:
      return 'primary';
    default:
      return 'danger'
  }
}

function loadIfEmpty(url, target) {
  if (target.html().trim().length == 0)
    target.load(url);
}

function getModal(event, link) {
  event.preventDefault();
  let url = $(this).attr('href') || $(this).attr('data-url') || link;
  let modal = $('#modal .modal-dialog');
  let clz = modal.attr('class');
  clz = clz.substring(0, clz.lastIndexOf(' '));
  clz += $(this).is('[modalsize]') ? ' modal-' + $(this).attr('modalsize') : ' modal-lg';
  modal.attr('class', clz);
  if (modal.html().trim().length == 0)
    modal.load(url, function (res, status, xhr) {
      if (status == 'error') {
        swal($.i18n.prop('error'), $.i18n.prop('resource_not_found'), 'error');
      } else {
        $('#modal').modal('show');
      }
    });
}

function postDelete(url, callback, title, text) {
  if (!confirm(LANG.areyousure + ' ' + title + '?\n' + text))
    return;
  $.ajax({
    url: url, type: 'POST',
    beforeSend: beforeSend,
    success: function (r) {
      callback()
    },
    error: ajaxError
  })
}

function postToggle(e, $elem, confText, success) {
  e.preventDefault();
  if (confirm(confText))
    $.ajax({
      url: $elem.attr('href'), method: 'post',
      beforeSend: beforeSend,
      success: success,
      error: ajaxError
    });
}

function plural(word, num) {
  let forms = word.split('_');
  return num % 10 === 1 && num % 100 !== 11 ? forms[0] : (num % 10 >= 2 && num % 10 <= 4 && (num % 100 < 10 || num % 100 >= 20) ? forms[1] : forms[2]);
}

function relativeTimeWithPlural(number, withoutSuffix, key) {
  let format = {
    'ii': withoutSuffix ? 'секунда_секунды_секунд' : 'секунду_секунды_секунд',
    'mm': withoutSuffix ? 'минута_минуты_минут' : 'минуту_минуты_минут',
    'hh': 'час_часа_часов',
    'dd': 'день_дня_дней',
    'MM': 'месяц_месяца_месяцев',
    'yy': 'год_года_лет'
  };
  if (key === 'm') return withoutSuffix ? 'минута' : 'минуту';
  if (key === 'i') return withoutSuffix ? 'минута' : 'минуту';
  else return number + ' ' + plural(format[key], +number);
}

function ajaxSubmitForm(form, success, error, before, dataFilter) {
  if (window.Parsley && !form.parsley().validate()) return;
  if (before == undefined || before == 'undefined') //todo default beforeSend callback for form submit
    before = beforeSend;
  if (success == undefined || success == 'undefined') //todo default success callback for form submit
    success = function (bindingErrors, textStatus, request) {
      if (bindingErrors.length) {
        form.parsley().validate();
        let errors = '';
        $(bindingErrors).each(function (i, e) {
          errors += LANG[e.field] + ' ' + (e.defaultMessage || e.code);
        });
        alert("--------------");
        alert(errors);

        return;
      }
      $('#datatable').DataTable().ajax.reload();
      $('#modal').modal('hide');
    };

  if (error == undefined || error == 'undefined') //todo default error callback for form submit
    error = ajaxError;
  if (dataFilter == undefined || dataFilter == 'undefined')
    dataFilter = function (data) {
      let disabled = form.find(':input:disabled').removeAttr('disabled');
      let data2 = form.serialize();
      disabled.attr('disabled', 'disabled');
      return data2;
    };

  $.ajax({
    type: form.attr('method'),
    url: form.attr('action'),
    data: dataFilter(form.serialize()),
    beforeSend: before,
    success: success,
    error: error
  });
}

function applyScheduleFrom(form) {
  ajaxSubmitForm(form, function (r) {
    $('#schedule').val(r.id);
    $('#sname').val(r.name).attr('placeholder', r.name);
    $('.schlink').attr('href', formLink + "/" + r.id);
    $('#modal').modal('hide');
  });
}

function updateDepended($elem, s, cb) {
  cb = cb || function (r) {
    let html = "";
    $(r).each(function (i, d) {
      html += '<option value="' + d.id + '">' + d.name + '</option>'
    });
    $elem.html(html).blur();
  };
  $.ajax({
    url: s,
    success: cb,
    error: ajaxError
  })
}

function initSpecLinks(parent) {
  if (parent == undefined) parent = '';
  $(document).on("click", parent + " .get-modal, a.get-delete", getModal);
}

function getLang() {
  if (navigator.languages != undefined)
    return navigator.languages[0];
  return navigator.language;
}

function toSnackCase(src) {
  return src.replace(/[A-Z]/g, letter => `_${letter.toLowerCase()}`);
}

$(document).ready(function () {
  initSpecLinks();

  $('#modal').on('hidden.bs.modal', function (e) {
    $('#modal .modal-content').html('');
  });

  $('#datatable').on('click', '.table__checkbox', function (e) {
    if ($(this).parents("tr").hasClass('selected__row')) {
      $(this).parents("tr").removeClass('selected__row')
    } else {
      $(this).parents("tr").addClass('selected__row');
    }
  });
  $('.toggleall').on('click', function (e) {
    $('.table__checkbox').prop('checked', $(this).prop('checked'));
    if ($(this).prop('checked')) {
      $('#datatable tr').addClass('selected__row');
    } else {
      $('#datatable tr').removeClass('selected__row');
    }
  });
});

function uuidv4() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
    let r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
    return v.toString(16);
  });
}

function select2_render($select2) {
  const order = $select2.data('preserved-order') || [];
  const $container = $select2.next('.select2-container');
  const $tags = $container.find('li.select2-selection__choice');
  const $input = $tags.last().next();

  // apply tag order
  order.forEach(function (val) {
    let $el = $tags.filter(function (i, tag) {
      return $(tag).data('data').id === val;
    });
    $input.before($el);
  });
}

function selectionHandler(e) {
  const $select2 = $(this);
  const val = e.params.data.id;
  const order = $select2.data('preserved-order') || [];

  if (e.type == 'select2:select') {
    order[order.length] = val;
  } else if (e.type == 'select2:unselect') {
    let found_index = order.indexOf(val);
    if (found_index >= 0)
      order.splice(found_index, 1);
  }

  $select2.data('preserved-order', order); // store it for later
  select2_render($select2, val);
}

(function ($) {
  $(window).on('load', function () {
    $("#preloader").delay(200).fadeOut();
    let url = $.urlParam('page');
    if (!url)
      url = $.urlParam('page', '?page=dashboard');
    $('.content').load(url, function (response, status, xhr) {
      if (status == 'error')
        history.back();
    });
  });

  $(window).on("popstate", function (e) {
    e.preventDefault();
    let url = $.urlParam('page', location.href);
    $.ajax({
      url: url,
      success: function (response) {
        $('.content').html(response);
      }
    });
    $("#sidebar-menu a.active").removeClass('active');
    //load dashboard
  });

  $('.modal').on('shown.bs.modal', function () {
    $(this).find('[autofocus]').focus();
  });

  $(".modal").on("hidden.bs.modal", function () {
    $(".modal-dialog").html("");
  });

  $('#sidebar-menu li:not([class])').on('click', 'a', function (e) {
    contentReload(e, $(this));
  });

  $('body').on('click', '.content-reload, .breadcrumb li a', function (e) {
    contentReload(e, $(this));
  });

  $(document).ajaxError(function (event, response) {
    if (response.status == 403)
      window.location.href = "login";
  });

  let socket = new SockJS('/monitor');
  let client = Stomp.over(socket);
  client.debug = f => f;
  client.connect({}, function (frame) {
    client.subscribe('/topic/newLeadsCount', function (message) {
      $('.requests-count').text(function () {
        $(this).text($(this).text() - 1);
      });
    });

    client.subscribe('/user/queue/trx', function (message) {
      let trx = JSON.parse(message.body);
      let title = $.i18n.prop('new_trx');
      let text = $.i18n.prop('terminal') + ': ' + (trx.terminal.id) + '<br/>' +
        $.i18n.prop('amount') + ': ' + (trx.amount / 100) + ' ' + trx.currency;
      $.Notification.notify('success', 'top right', title, text);
      let page = $.urlParam('page');
      switch (page) {
        case 'transactions':
          $('#datatable').DataTable().ajax.reload();
          break;
        case 'merchant':
          $.event.trigger({
            type: "transaction-received",
            message: trx
          });
      }
    });

    client.subscribe('/user/queue/reconciliation', function (message) {
      let reco = JSON.parse(message.body);
      let title = $.i18n.prop('reconciliation');
      let text = $.i18n.prop('terminal') + ': ' + (reco.posId) + '<br/>' +
        $.i18n.prop('amount_net') + ': ' + (reco.amountNet / 100).toLocaleString() + ' ' + reco.currency;
      $.Notification.notify('success', 'top right', title, text);
      let page = $.urlParam('page');
      if (page == 'reconciliations') {
        $('#datatable').DataTable().ajax.reload();
      }
    });

    client.subscribe('/topic/logs', function (message) {
      $('.logs-list').append(message.body);
    });

  });
})(jQuery);
/*]]>*/