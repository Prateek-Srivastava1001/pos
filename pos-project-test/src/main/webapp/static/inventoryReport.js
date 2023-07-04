
function getInventoryReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/inventory";
}


function getInventoryList(){
	var url = getInventoryReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryReportList(data);
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS
filteredData = []
function displayInventoryReportList(data){
	var $tbody = $('#inventory-report-table').find('tbody');
	$tbody.empty();
	filteredData=data
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	if(filteredData.length>0){
	    $("#download-report").removeAttr("disabled");
	}
}

function downloadReport(){
    var headers = {
        brand: 'brand'.replace(/,/g, ''), // remove commas to avoid errors
        category: "category",
        quantity: "quantity"
    };
    var dataFormatted = [];

    // format the data
    filteredData.forEach((item) => {
        dataFormatted.push({
            brand: item.brand.replace(/,/g, ''), // remove commas to avoid errors,
            category: item.category.replace(/,/g, ''),
            quantity: item.quantity
        });
    });

    var fileTitle = 'InventoryReport';
    exportCSVFile(headers, dataFormatted, fileTitle);
}
//INITIALIZATION CODE
function init(){
    $("#download-report").click(downloadReport);
}
$(document).ready(getInventoryList);
$(document).ready(init);
