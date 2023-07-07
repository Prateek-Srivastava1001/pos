
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
	result = data.map(o => {
          let obj = Object.assign({}, o);
          delete obj.id;
          return obj;
        });
	filteredData=result;
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
    var fileName = 'BrandReport.tsv';
    writeReportData(filteredData, fileName);
}

//INITIALIZATION CODE
function init(){
    $("#download-report").click(downloadReport);
}

$(document).ready(getBrandList);
$(document).ready(init);
