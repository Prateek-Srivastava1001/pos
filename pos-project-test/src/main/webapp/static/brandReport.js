var table;
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
	table.clear().draw();
	result = data.map(o => {
          let obj = Object.assign({}, o);
          delete obj.id;
          return obj;
        });
	filteredData=result;
	for(var i in data){
		var e = data[i];
		table.row.add([e.brand, e.category]).draw();

	}
	if(data.length>0){
	    $("#download-report").removeAttr("disabled");
	}
}
function downloadReport(){
    var fileName = 'BrandReport.tsv';
    if(filteredData.length>0){
    writeReportData(filteredData, fileName);
    successClick("Report Downloaded Successfully");
    }
    else{
        warnClick("Empty Report")
    }
}

//INITIALIZATION CODE
function init(){
    $("#download-report").click(downloadReport);
    table = $('#brand-report-table').DataTable({searching: false,
                                                    'columnDefs': [{'targets': [0,1], "className": "text-center"}],
                                                    info: false,
                                                    lengthMenu: [
                                                                 [15, 25, 50, -1],
                                                                  [15, 25, 50, 'All']
                                                                 ]
        });
}

$(document).ready(getBrandList);
$(document).ready(init);
