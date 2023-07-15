var table;
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
	table.clear().draw();
	result = data.map(o => {
          let obj = Object.assign({}, o);
          delete obj.revenue;
          return obj;
        });
	filteredData=result;
	for(var i in data){
		var e = data[i];
		table.row.add([e.brand, e.category, e.quantity]).draw();

	}
	if(filteredData.length>0){
	    $("#download-report").removeAttr("disabled");
	}
}

function downloadReport(){
    var fileName = 'InventoryReport.tsv';
    if(filteredData.length>0){
    writeReportData(filteredData, fileName);
    successClick("Report Downloaded Successfully");
    }
    else{
    warnClick("Empty Report");
    }
}
//INITIALIZATION CODE
function init(){
    $("#download-report").click(downloadReport);
    table = $('#inventory-report-table').DataTable({searching: false,
                                                        'columnDefs': [{'targets': [0,1,2], "className": "text-center"}],
                                                        info: false,
                                                        lengthMenu: [
                                                                     [15, 25, 50, -1],
                                                                      [15, 25, 50, 'All']
                                                                     ]
            });
}
$(document).ready(getInventoryList);
$(document).ready(init);
