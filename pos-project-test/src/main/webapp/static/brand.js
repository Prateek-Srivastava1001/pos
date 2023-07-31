var table;
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}
function getAdminBrandUrl(){
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    	return baseUrl + "/api/admin/brand";
}
//BUTTON ACTIONS
function addBrand(event){
	//Set the values to update
	var $form = $("#brand-form");
	var formData = $form.serializeArray();
	var brand = formData[0].value;
	var category = formData[1].value;
	if(brand==null ||brand==""){
        warnClick("Brand cannot be empty");
        return;
    }
    if(category==null ||category==""){
        warnClick("Category cannot be empty");
        return;
    }
	var json = toJson($form);
	var url = getAdminBrandUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        successClick("Data added successfully");
	        $("#brand-form input[name=brand]").val("");
	        $("#brand-form input[name=category]").val("");
	   		getBrandList();
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateBrand(event){
	$('#edit-brand-modal').modal('toggle');
	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();
	var url = getAdminBrandUrl() + "/" + id;

	//Set the values to update
	var $form = $("#brand-edit-form");
	var formData = $form.serializeArray();
    	var brand = formData[0].value;
    	var category = formData[1].value;
    	if(brand==null ||brand==""){
            warnClick("Brand cannot be empty");
            return true;
        }
        if(category==null ||category==""){
            warnClick("Category cannot be empty");
            return true;
        }
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        successClick("Data updated successfully");
	   		getBrandList();
	   },
	   error: handleAjaxError
	});

	return false;
}


function getBrandList(){
    table.clear().draw();
    table.row.add(["","<i class='fa fa-refresh fa-spin'></i>",""]).draw();
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandList(data);
	   },
	   error: handleAjaxError
	});

}


// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#brandFile')[0].files[0];
    //Checking for .tsv extension
	var extension = getExtension($('#brandFile').val());
    if(extension.toLowerCase() != 'tsv'){
    dangerClick('Please Upload File with extension .tsv ');
    console.log("INVALID FILE TYPE...");
    return;
    }
    readFileData(file, readFileDataCallback);
}
function getExtension(filename) {
  var parts = filename.split('.');
  return parts[parts.length - 1];
}
function readFileDataCallback(results){
	fileData = results.data;
	if(fileData.length>5000){
	    dangerClick("Cannot upload a file with more than 5000 lines");
	    return;
	}
    const columnHeaders = Object.keys(fileData[0]);
    const expectedHeaders = ["brand","category"];
    const headersMatched = expectedHeaders.every(header => columnHeaders.includes(header));
    if(headersMatched && columnHeaders.length === expectedHeaders.length){
        uploadRows();
    }
    else{
        warnClick("Invalid TSV, Headers must include both 'brand' and 'category' only.");
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
    	successClick("File upload Successful")
    	}
    	getBrandList();
    	document.getElementById("process-data").disabled=true;
		return;
	}

	//Process next row
	var row = fileData[processCount];
	processCount++;

	var json = JSON.stringify(row);
	var url = getAdminBrandUrl();
	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		uploadRows();
	   },
	   error: function(response){
	        row.lineNumber=processCount+1;
	   		row.error=response.responseText;
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayBrandList(data){

	var $tbody = $('#brand-table').find('tbody');
	table.clear().draw();
	var dataRows = [];
	for(var i in data){
		var e = data[i];
		var maxLength = 25;
		var roleElement = document.getElementById('role');
        var role = roleElement.innerText;
        if(role=="operator"){
        var buttonHtml = ' <button class="btn btn-outline-danger edit_btn" disabled>Edit</button>'
        }
        else
		    var buttonHtml = ' <button class="btn btn-outline-info" onclick="displayEditBrand(' + e.id + ')">Edit</button>';
        dataRows.push([e.brand, e.category, buttonHtml]);
	}

	table.rows.add(dataRows).draw();
}

function displayEditBrand(id){
	var url = getBrandUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrand(data);
	   },
	   error: handleAjaxError
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#brandFile');
	$file.val('');
	$('#brandFileName').html("Choose File");
	$("#process-data").removeAttr("disabled");
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
	var $file = $('#brandFile');
	var fileName = $file.val().replace(/.*(\/|\\)/, '');
	$('#brandFileName').html(fileName);
	$("#process-data").removeAttr("disabled");
	document.getElementById("download-errors").disabled = true;
	processCount = 0;
    	fileData = [];
    	errorData = [];
    	//Update counts
    	updateUploadDialog();
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-brand-modal').modal('toggle');
	document.getElementById("download-errors").disabled = true;
}

function displayBrand(data){
	$("#brand-edit-form input[name=brand]").val(data.brand);
	$("#brand-edit-form input[name=category]").val(data.category);
	$("#brand-edit-form input[name=id]").val(data.id);
	$('#edit-brand-modal').modal('toggle');
}
function refresh(){
    location.reload(true);
}

//INITIALIZATION CODE
function init(){
	$('#add-brand').click(addBrand);
	$('#update-brand').click(updateBrand);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName);
    var roleElement = document.getElementById('role');
    var role = roleElement.innerText;

    if(role=="operator"){
        document.getElementById("add-brand").disabled = true;
        document.getElementById("update-brand").disabled = true;
        document.getElementById("process-data").disabled = true;
        document.getElementById("download-errors").disabled = true;
        document.getElementById("upload-data").disabled=true;
        document.getElementById("brand-form").innerHTML = "";
        document.getElementById("edit-brand-modal").innerHTML = "";
//      document.getElementById("upload-brand-modal").innerHTML = "";
    }
    document.getElementById("download-errors").disabled = true;
    table = $('#brand-table').DataTable({'columnDefs': [
        {'targets': [2],'orderable': false },
        {'targets': [0,1,2], "className": "text-center"}
         ],
         info:false,
         lengthMenu: [
                 [10, 25, 50, -1],
                 [10, 25, 50, 'All']
             ],
         deferRender: true
    });
}
$(document).ready(init);
$(document).ready(getBrandList);



