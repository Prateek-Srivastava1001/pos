
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

//UI DISPLAY METHODS

function displaySchedulerReportList(data){
	var $tbody = $('#brand-report-table').find('tbody');
	$tbody.empty();
	for(var i in data){
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

//INITIALIZATION CODE
$(document).ready(getSchedulerList);
