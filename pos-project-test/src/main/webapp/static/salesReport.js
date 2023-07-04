
function getSalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/sales";
}


function getSalesList(event) {
    var dateInput = document.getElementById("inputSD");
    var dateInput2 = document.getElementById("inputED");
    if((!dateInput.value) || (!dateInput2.value) ){
        alert("Do not keep date field empty");
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
    $tbody.empty();
    var brand = document.forms["brand-form"]["brand"].value;
    var category = document.forms["brand-form"]["category"].value;
    if(brand == null || brand ==""){
        alert("Please fill Brand Data before applying filter");
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
                var row = '<tr>'
                		+ '<td>' + element.brand + '</td>'
                		+ '<td>' + element.category + '</td>'
                		+ '<td>' + element.quantity + '</td>'
                		+ '<td>' + element.revenue + '</td>'
                		+ '</tr>';
                $tbody.append(row);
                filteredData.push(element);
                flag=1;
            }
            else if(element.category == category){
            var row = '<tr>'
                    + '<td>' + element.brand + '</td>'
                    + '<td>' + element.category + '</td>'
                    + '<td>' + element.quantity + '</td>'
                    + '<td>' + element.revenue + '</td>'
                    + '</tr>';
            $tbody.append(row);
            filteredData.push(element);
            flag=1;
            }
        }
    }

    if(flag == 0){
        var row = '<tr><td>No transaction for given brand - category found in given timeframe</td></tr>';
        $tbody.append(row);
        document.getElementById("download-report").disabled = true;
    }
    else{
        $("#download-report").removeAttr("disabled");
    }

}
function displaySalesReportList(data){
	var $tbody = $('#brand-report-table').find('tbody');
	$tbody.empty();
	initialData = data;
	filteredData = data;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + e.revenue + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	if(data.length < 1){
	    var row = '<tr><td>No transaction for given timeframe found</td></tr>';
	    $tbody.append(row);
	    document.getElementById("download-report").disabled = true;
	}
	else{
	    $("#download-report").removeAttr("disabled");
	}
	$("#apply-brand-filter").removeAttr("disabled");
	$("#refresh-data").removeAttr("disabled");
}

function downloadReport(){
    var headers = {
        brand: 'brand'.replace(/,/g, ''), // remove commas to avoid errors
        category: "category",
        quantity: "quantity",
        revenue: "revenue"
    };
    var dataFormatted = [];

    // format the data
    filteredData.forEach((item) => {
        dataFormatted.push({
            brand: item.brand.replace(/,/g, ''), // remove commas to avoid errors,
            category: item.category.replace(/,/g, ''),
            quantity: item.quantity,
            revenue: item.revenue
        });
    });

    var fileTitle = 'SalesReport';
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
 }
$(document).ready(init);

