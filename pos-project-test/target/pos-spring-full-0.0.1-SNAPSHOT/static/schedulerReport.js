var table;
function getSchedulerReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/scheduler";
}

function getGenerateReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/scheduler/generate";
}

function getSchedulerList(){
table.clear().draw();
table.row.add(["","Processing...","<i class='fa fa-refresh fa-spin'></i>",""]).draw();
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

function generateReport(){
	var url = getGenerateReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		getSchedulerList();
	   		successClick("Report Generated Successfully");
	   },
	   error: handleAjaxError
	});
}

function getFilteredList(event) {
    var dateInput = document.getElementById("inputSD");
    var dateInput2 = document.getElementById("inputED");
    if((!dateInput.value) || (!dateInput2.value) ){
        warnClick("Empty date field not supported");
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
        successClick("Date filter applied successfully");
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
	var dataRows = [];
	for(var i in data){
	if(data.length > 0){
    	    $("#download-report").removeAttr("disabled");
    	}
		var e = data[i];
        dataRows.push([e.date, e.invoiced_orders_count, e.invoiced_items_count,
                        'Rs '+(Math.round(parseFloat(e.total_revenue)*100)/100).toFixed(2)]);
	}
	table.rows.add(dataRows).draw();
}

function resetForm() {
  var element = document.getElementById("sales-form");
  element.reset();
}
function downloadReport(){
    var fileName = 'SchedulerReport.tsv';
    if(filteredData.length>0){
    writeReportData(filteredData, fileName);
    successClick("Report Downloaded Successfully");
    }
    else{
        warnClick("Report is empty");
    }
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
   $("#download-report").click(downloadReport);
   $("#generate-report").click(generateReport);
    var dateInput = document.getElementById("inputSD");
    var dateInput2 = document.getElementById("inputED");
    var today = new Date();
    dateInput.setAttribute("max", today.toISOString().substring(0, 10));
    dateInput2.setAttribute("max", today.toISOString().substring(0, 10));
    table = $('#brand-report-table').DataTable({'columnDefs': [ {'targets': [1,2,3],'orderable': false },
                                                                {'targets': [0,1,2,3], "className": "text-center"}],
                                                                 searching: false,
                                                                 info:false,
                                                                 lengthMenu: [
                                                                         [15, 25, 50, -1],
                                                                         [15, 25, 50, 'All']
                                                                     ],
                                                                 order: [[0, 'desc']],
                                                                 deferRender: true
    });
 }
 $(document).ready(init);
$(document).ready(getSchedulerList);


