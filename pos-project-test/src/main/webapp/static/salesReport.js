var table;
function getSalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/sales";
}


function getSalesList(event) {
table.clear().draw();
table.row.add(["","Processing...","<i class='fa fa-refresh fa-spin'></i>",""]).draw();
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
        successClick("Report generated for given timeframe");
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
    var $form = $("#brand-form");
    var formData = $form.serializeArray();
//    var brand = document.forms["brand-form"]["brand"].value;
    var brand = formData[0].value;
    var category = formData[1].value;
    if(brand == null || brand ==""){
        refreshData();
        return;
    }
    table.clear().draw();
    var dataRows = [];
    brand = brand.toLowerCase().trim();
    category = category.toLowerCase().trim();
    var flag = 0;
    filteredData = [];
    for(var i in initialData){
        var element = initialData[i];
        if(element.brand == brand){
            if(category == null || category == ""){
                dataRows.push([
                                element.brand,
                                element.category,
                                element.quantity,
                                'Rs '+(Math.round(parseFloat(element.revenue)*100)/100).toFixed(2)
                               ]);
                filteredData.push(element);
                flag=1;
            }
            else if(element.category == category){
            dataRows.push([
                            element.brand,
                            element.category,
                            element.quantity,
                            'Rs '+(Math.round(parseFloat(element.revenue)*100)/100).toFixed(2)
                           ]);
            filteredData.push(element);
            flag=1;
            }
        }

    }
    table.rows.add(dataRows).draw();

    if(flag == 0){
        document.getElementById("download-report").disabled = true;
    }
    else{
        $("#download-report").removeAttr("disabled");
    }
successClick("Filter applied successfully");
}
function displaySalesReportList(data){
	var $tbody = $('#brand-report-table').find('tbody');
	table.clear().draw();
	var dataRows = [];
	initialData = data;
	filteredData = data;
	for(var i in data){
		var e = data[i];
        dataRows.push([
                       e.brand,
                       e.category,
                       e.quantity,
                       'Rs '+(Math.round(parseFloat(e.revenue)*100)/100).toFixed(2)
                       ])
	}
	table.rows.add(dataRows).draw();
	if(data.length < 1){
	    document.getElementById("download-report").disabled = true;
	}
	else{
	    $("#download-report").removeAttr("disabled");
	}
	$("#apply-brand-filter").removeAttr("disabled");
	$("#refresh-data").removeAttr("disabled");
	populateBrandDropdown();
	populateCategoryDropdown(null);
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
    displaySalesReportList(initialData);
    successClick("Data refreshed");
}
function populateBrandDropdown(){
    var brandDropdown = $('#brand');
    // Clear existing options
    brandDropdown.empty();
    // Add an empty option
    brandDropdown.append($('<option>', {
        value: '',
        text: 'Select'
    }));
    var distinctBrands = new Set();

    // Iterate over the brand-category data to collect distinct brands
    initialData.forEach(function(item) {
       distinctBrands.add(item.brand);
    });

    distinctBrands.forEach(function(brand) {
            brandDropdown.append($('<option>', {
                value: brand,
                text: brand
            }));
        });
}
function populateCategoryDropdown(selectedBrand){
    var categoryDropdown = $('#category');
    categoryDropdown.empty();
    categoryDropdown.append($('<option>', {
            value: '',
            text: 'Select'
        }));
    // Filter the brand-category data based on the selected brand
    var filteredData = initialData.filter(function(item) {
        return item.brand === selectedBrand;
    });

    filteredData.forEach(function(item) {
            categoryDropdown.append($('<option>', {
                value: item.category,
                text: item.category
            }));
        });
}
//INITIALIZATION CODE
function init() {
   $("#apply-filter").click(getSalesList);
   $("#apply-brand-filter").click(displayFilteredReport);
   $("#refresh-data").click(refreshData);
   $("#download-report").click(downloadReport);
   $('#brand').on('change', function() {
                                               var selectedBrand = $(this).val();
                                                populateCategoryDropdown(selectedBrand);
                                             });
    var dateInput = document.getElementById("inputSD");
    var dateInput2 = document.getElementById("inputED");
    var today = new Date();
    dateInput.setAttribute("max", today.toISOString().substring(0, 10));
    dateInput2.setAttribute("max", today.toISOString().substring(0, 10));
    table = $('#brand-report-table').DataTable({searching: false,
                                                'columnDefs': [{'targets': [0,1,2,3], "className": "text-center"}],
                                                info: false
    });
    $('.select2').select2();
 }
$(document).ready(init);

