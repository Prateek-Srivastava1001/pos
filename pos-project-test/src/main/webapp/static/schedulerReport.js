
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
        alert("Do not keep date field empty");
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
	$tbody.empty();
	for(var i in data){
	if(data.length > 0){
    	    $("#download-report").removeAttr("disabled");
    	}
		var e = data[i];
		console.log(e);
		var row = '<tr>'
		+ '<td>' + e.date + '</td>'
		+ '<td>' + e.invoiced_orders_count + '</td>'
		+ '<td>' + e.invoiced_items_count + '</td>'
		+ '<td>' + e.total_revenue + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function resetForm() {
  var element = document.getElementById("sales-form");
  element.reset();
}
function downloadReport(){
    var headers = {
        date: 'date'.replace(/,/g, ''), // remove commas to avoid errors
        invoiced_orders_count: "invoiced_orders_count",
        invoiced_items_count: "invoiced_items_count",
        total_revenue: "total_revenue"
    };
    var dataFormatted = [];

    // format the data
    filteredData.forEach((item) => {
        dataFormatted.push({
            date: item.date.replace(/,/g, ''), // remove commas to avoid errors,
            invoiced_orders_count: item.invoiced_orders_count,
            invoiced_items_count: item.invoiced_items_count,
            total_revenue: item.total_revenue
        });
    });

    var fileTitle = 'DailySalesReport';
    exportCSVFile(headers, dataFormatted, fileTitle);
}

function validateDate(input) {
  var dateFormat = /^\d{4}-\d{2}-\d{2}$/;
  var today = new Date();
  if(!input.value){
    alert("Date field cannot be empty");
  }
  var inputDate = new Date(input.value);
     if (inputDate > today) {
        alert("Input date cannot be after today's date.");
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
 }
$(document).ready(getSchedulerList);
$(document).ready(init);

