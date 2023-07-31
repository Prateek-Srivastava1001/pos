var table;
function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}
function getAdminInventoryUrl(){
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/admin/inventory";
}

//BUTTON ACTIONS
function updateInventory(event){
	$('#edit-inventory-modal').modal('toggle');
	//Get the ID
	var id = $("#inventory-edit-form input[name=id]").val();
	var url = getAdminInventoryUrl() + "/" + id;

	//Set the values to update
	var $form = $("#inventory-edit-form");
	var formData = $form.serializeArray();
	formData[0].value = +formData[0].value;
	if(parseFloat(formData[0].value)%1!==0){
        warnClick("Please enter a valid integer value for quantity");
        return;
    }
    if(parseFloat(formData[0].value)>10000000){
        warnClick("Maximum value of quantity can be 10000000");
        return;
    }
    if(parseFloat(formData[0].value)<0){
        warnClick("Quantity cannot be negative");
        return;
    }
	var json = fromSerializedToJson(formData);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        successClick("Data updated successfully");
	   		getInventoryList();
	   },
	   error: handleAjaxError
	});

	return false;
}


function getInventoryList(){
table.clear().draw();
table.row.add(["","<i class='fa fa-refresh fa-spin'></i>",""]).draw();
	var url = getInventoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryList(data);
	   },
	   error: handleAjaxError
	});
}


// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#inventoryFile')[0].files[0];
	//Checking for .tsv extension
    	var extension = getExtension($('#inventoryFile').val());
    	console.log(extension);
        if(extension.toLowerCase() != 'tsv'){
        dangerClick('Please Upload File with extension .tsv');
        console.log("INVALID FILE TYPE...");
        return;
        }
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	if(fileData.length>5000){
    	    dangerClick("Cannot upload a file with more than 5000 lines");
    	    return;
    	}
	const columnHeaders = Object.keys(fileData[0]);
    const expectedHeaders = ["barcode","quantity"];
    const headersMatched = expectedHeaders.every(header => columnHeaders.includes(header));
    if(headersMatched && columnHeaders.length === expectedHeaders.length){
        uploadRows();

    }
    else{
        warnClick("Invalid TSV, Headers must include both 'barcode' and 'quantity' only.");
        return;
    }
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
	    if(errorData.length>0){
	        $("#download-errors").removeAttr("disabled");
	        warnClick("There were some issues with the uploaded file, please download errors to find more");
	    }
	    else{
	    successClick("File upload successful");
	    }
	    getInventoryList();
	    document.getElementById("process-data").disabled=true;
		return;
	}

	//Process next row
	var row = fileData[processCount];
	console.log(row);
	processCount++;
	if(parseFloat(row.quantity)>10000000){
    	    row.lineNumber=processCount+1;
            row.error="quantity cannot be more than 10000000";
            errorData.push(row);
            uploadRows();
            return;
    	}
	var json = JSON.stringify(row);
	var url = getAdminInventoryUrl();
	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		uploadRows();
	   },
	   error: function(response){
	        row.lineNumber=processCount+1;
	   		row.error=response.responseText
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayInventoryList(data){
	var $tbody = $('#inventory-table').find('tbody');
	table.clear().draw();
	var dataRows = []
	for(var i in data){
		var e = data[i];
		var roleElement = document.getElementById('role');
        var role = roleElement.innerText;
        if(role=="operator"){
            var buttonHtml = ' <button class="btn btn-outline-danger edit_btn" disabled>Edit</button>';
        }
        else
		    var buttonHtml = ' <button class="btn btn-outline-info" onclick="displayEditInventory(' + e.id + ')">edit</button>';
        dataRows.push([e.barcode,e.quantity, buttonHtml]);
	}
	table.rows.add(dataRows).draw();
}

function displayEditInventory(id){
	var url = getInventoryUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventory(data);
	   },
	   error: handleAjaxError
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts
	updateUploadDialog();
	$("#process-data").removeAttr("disabled");
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#inventoryFile');
	$("#process-data").removeAttr("disabled");
	var fileName = $file.val().replace(/.*(\/|\\)/, '');
	$('#inventoryFileName').html(fileName);
	document.getElementById("download-errors").disabled = true;
    	processCount = 0;
        	fileData = [];
        	errorData = [];
        	//Update counts
        	updateUploadDialog();
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-inventory-modal').modal('toggle');
}

function displayInventory(data){
	$("#inventory-edit-form input[name=quantity]").val(data.quantity);
	$("#inventory-edit-form input[name=id]").val(data.id);
	$('#edit-inventory-modal').modal('toggle');
}

function getExtension(filename) {
    console.log(filename);
  var parts = filename.split('.');
  console.log(parts);
  return parts[parts.length - 1];
}
 function fromSerializedToJson(serialized){
     var s = '';
     var data = {};
     for(s in serialized){
         data[serialized[s]['name']] = serialized[s]['value']
     }
     var json = JSON.stringify(data);
     return json;
 }
//INITIALIZATION CODE
function init(){
	$('#update-inventory').click(updateInventory);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#inventoryFile').on('change', updateFileName);

    var roleElement = document.getElementById('role');
    var role = roleElement.innerText;

    if(role=="operator"){
        document.getElementById("update-inventory").disabled = true;
        document.getElementById("upload-data").disabled = true;
        document.getElementById("process-data").disabled = true;
        document.getElementById("inventory-form").innerHTML = "";
        document.getElementById("edit-inventory-modal").innerHTML = "";
    }
    document.getElementById("download-errors").disabled = true;
    table = $('#inventory-table').DataTable({'columnDefs': [ {'targets': [2],'orderable': false },
                    {'targets': [0,1,2], "className": "text-center"}],
                 info:false,
        lengthMenu: [
                [15, 25, 50, -1],
                [15, 25, 50, 'All']
            ],
            deferRender: true
    });
}

$(document).ready(init);
$(document).ready(getInventoryList);
