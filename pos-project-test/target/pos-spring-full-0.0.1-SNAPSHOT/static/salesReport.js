var table;
function getSalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/sales";
}


function getSalesList(event) {
    var dateInput = document.getElementById("inputSD");
    var dateInput2 = document.getElementById("inputED");
    if((!dateInput.value) || (!dateInput2.value) ){
        warnClick("Empty date field not supported");
        return;
    }
    var $form = $("#sales-form");
    var json = toJson($form);
    var url = getSalesReportUrl();
    $.ajax({
        url: url,
        type: "POST",
        data: json,
        headers: {
        "Content-Type": "application/json",
    },
    success: function (response) {
        displaySalesReportList(response);
    },
    error: handleAjaxError,
  });

  return false;
}

let initialData = [];
let filteredData = [];
//UI DISPLAY METHODS

function displayFilteredReport(){
    var $tbody = $('#brand-report-table').find('tbody');
    table.clear().draw();
    var brand = document.forms["brand-form"]["brand"].value;
    var category = document.forms["brand-form"]["category"].value;
    if(brand == null || brand ==""){
        warnClick("Please fill Brand Data before applying filter");
        return;
    }
    brand = brand.toLowerCase().trim();
    category = category.toLowerCase().trim();
    var flag = 0;
    filteredData = [];
    for(var i in initialData){
        var element = initialData[i];
        if(element.brand == brand){
            if(category == null || category == ""){
                table.row.add([
                              element.brand,
                              element.category,
                              element.quantity,
                              element.revenue
                                ]).draw();
                filteredData.push(element);
                flag=1;
            }
            else if(element.category == category){
            table.row.add([
                           element.brand,
                           element.category,
                           element.quantity,
                           element.revenue
                            ]).draw();
            filteredData.push(element);
            flag=1;
            }
        }
    }

    if(flag == 0){
        document.getElementById("download-report").disabled = true;
    }
    else{
        $("#download-report").removeAttr("disabled");
    }

}
function displaySalesReportList(data){
	var $tbody = $('#brand-report-table').find('tbody');
	table.clear().draw();
	initialData = data;
	filteredData = data;
	for(var i in data){
		var e = data[i];
        table.row.add([
                       e.brand,
                       e.category,
                       e.quantity,
                       e.revenue
                        ]).draw();
	}
	if(data.length < 1){
	    document.getElementById("download-report").disabled = true;
	}
	else{
	    $("#download-report").removeAttr("disabled");
	}
	$("#apply-brand-filter").removeAttr("disabled");
	$("#refresh-data").removeAttr("disabled");
}

function downloadReport(){
    var fileName = 'SalesReport.tsv';
    if(filteredData.length>0){
    writeReportData(filteredData, fileName);
    successClick("Report Downloaded Successfully");
    }
    else{
        warnClick("Empty report");
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
function refreshData(){
    console.log("inside refresh");
    displaySalesReportList(initialData);
}
//INITIALIZATION CODE
function init() {
   $("#apply-filter").click(getSalesList);
   $("#apply-brand-filter").click(displayFilteredReport);
   $("#refresh-data").click(refreshData);
   $("#download-report").click(downloadReport);

    var dateInput = document.getElementById("inputSD");
    var dateInput2 = document.getElementById("inputED");
    var today = new Date();
    dateInput.setAttribute("max", today.toISOString().substring(0, 10));
    dateInput2.setAttribute("max", today.toISOString().substring(0, 10));
    table = $('#brand-report-table').DataTable({searching: false});
 }
$(document).ready(init);

