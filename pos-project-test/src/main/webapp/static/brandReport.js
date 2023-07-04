
function getBrandReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}


function getBrandList(){
	var url = getBrandReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandReportList(data);
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS
filteredData =[];
function displayBrandReportList(data){
	var $tbody = $('#brand-report-table').find('tbody');
	$tbody.empty();
	filteredData=data;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	if(data.length>0){
	    $("#download-report").removeAttr("disabled");
	}
}
function downloadReport(){
    var headers = {
        brand: 'brand'.replace(/,/g, ''), // remove commas to avoid errors
        category: "category".replace(/,/g, '')
    };
    var dataFormatted = [];

    // format the data
    filteredData.forEach((item) => {
        dataFormatted.push({
            brand: item.brand.replace(/,/g, ''), // remove commas to avoid errors,
            category: item.category.replace(/,/g, '')
        });
    });

    var fileTitle = 'BrandReport';
    exportCSVFile(headers, dataFormatted, fileTitle);
}

//INITIALIZATION CODE
function init(){
    $("#download-report").click(downloadReport);
}

$(document).ready(getBrandList);
$(document).ready(init);
