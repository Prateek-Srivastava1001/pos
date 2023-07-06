
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
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getInventoryList();
	   },
	   error: handleAjaxError
	});

	return false;
}


function getInventoryList(){
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
        alert('Please Upload File with extension .tsv only...');
        console.log("INVALID FILE TYPE...");
        return;
        }
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	if(fileData.length>5000){
    	    alert("Cannot upload a file with more than 5000 lines");
    	    return;
    	}
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
	    getInventoryList();
	    if(errorData.length>0){
	        $("#download-errors").removeAttr("disabled");
	    }
		return;
	}

	//Process next row
	var row = fileData[processCount];
	processCount++;
	var json = JSON.stringify(row);
	console.log(json);
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
	        row.lineNumber=processCount;
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
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var roleElement = document.getElementById('role');
        var role = roleElement.innerText;
        if(role=="operator"){
            var buttonHtml = ' <button class="edit_btn" disabled>edit</button>'
        }
        else
		    var buttonHtml = ' <button onclick="displayEditInventory(' + e.id + ')">edit</button>'
		var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
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
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#inventoryFile');
	var fileName = $file.val().replace(/.*(\/|\\)/, '');
	$('#inventoryFileName').html(fileName);
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
    }
    document.getElementById("download-errors").disabled = true;
}

$(document).ready(init);
$(document).ready(getInventoryList);
