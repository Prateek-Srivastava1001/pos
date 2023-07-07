var table;
function getSchedulerReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/scheduler";
}


function getSchedulerList(){
	var url = getSchedulerReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displaySchedulerReportList(data);
	   },
	   error: handleAjaxError
	});
}

function getFilteredList(event) {
    var dateInput = document.getElementById("inputSD");
    var dateInput2 = document.getElementById("inputED");
    if((!dateInput.value) || (!dateInput2.value) ){
        warnClick("Do not keep date field empty");
        return;
    }
    var $form = $("#sales-form");
    var json = toJson($form);
    var url = getSchedulerReportUrl();

    $.ajax({
        url: url,
        type: "POST",
        data: json,
        headers: {
        "Content-Type": "application/json",
    },
    success: function (response) {
        resetForm();
        displaySchedulerReportList(response);
    },
    error: handleAjaxError,
  });

  return false;
}


//UI DISPLAY METHODS
let filteredData = [];
function displaySchedulerReportList(data){
    filteredData = data;
	var $tbody = $('#brand-report-table').find('tbody');
	table.clear().draw();
	for(var i in data){
	if(data.length > 0){
    	    $("#download-report").removeAttr("disabled");
    	}
		var e = data[i];
		console.log(e);
        table.row.add([
                      e.date,
                      e.invoiced_orders_count,
                      e.invoiced_items_count,
                      e.total_revenue
                    ]).draw();
	}
}

function resetForm() {
  var element = document.getElementById("sales-form");
  element.reset();
}
function downloadReport(){
    var fileName = 'SchedulerReport.tsv';
    writeReportData(filteredData, fileName);
}

function validateDate(input) {
  var dateFormat = /^\d{4}-\d{2}-\d{2}$/;
  var today = new Date();
  if(!input.value){
    warnClick("Date field cannot be empty");
  }
  var inputDate = new Date(input.value);
     if (inputDate > today) {
        warnClick("Input date cannot be after today's date.");
    input.value = "";
  } else {
    input.setCustomValidity("");
  }
}
//INITIALIZATION CODE
function init() {
   $("#apply-filter").click(getFilteredList);
   $("#download-report").click(downloadReport)
    var dateInput = document.getElementById("inputSD");
    var dateInput2 = document.getElementById("inputED");
    var today = new Date();
    dateInput.setAttribute("max", today.toISOString().substring(0, 10));
    dateInput2.setAttribute("max", today.toISOString().substring(0, 10));
    table = $('#brand-report-table').DataTable();
 }
$(document).ready(getSchedulerList);
$(document).ready(init);

