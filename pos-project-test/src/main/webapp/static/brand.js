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
	console.log(json);
	var url = getAdminBrandUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		refresh();
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
            dangerClick("Brand cannot be empty");
            return true;
        }
        if(category==null ||category==""){
            dangerClick("Category cannot be empty");
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
	   		getBrandList();
	   },
	   error: handleAjaxError
	});

	return false;
}


function getBrandList(){
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
	console.log(extension);
    if(extension.toLowerCase() != 'tsv'){
    dangerClick('Please Upload File with extension .tsv only...');
    console.log("INVALID FILE TYPE...");
    return;
    }
    readFileData(file, readFileDataCallback);
}
function getExtension(filename) {
    console.log(filename);
  var parts = filename.split('.');
  console.log(parts);
  return parts[parts.length - 1];
}
function readFileDataCallback(results){
	fileData = results.data;
	if(fileData.length>5000){
	    dangerClick("Cannot upload a file with more than 5000 lines");
	    return;
	}
	uploadRows();
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
		return;
	}

	//Process next row
	var row = fileData[processCount];
	processCount++;

	var json = JSON.stringify(row);
	var url = getAdminBrandUrl();
    console.log(json);
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
	        row.lineNumber=processCount;
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
	for(var i in data){
		var e = data[i];
		var maxLength = 25;
		var roleElement = document.getElementById('role');
        var role = roleElement.innerText;
        if(role=="operator"){
        var buttonHtml = ' <button class="edit_btn" onclick="displayEditBrand(' + e.id + ')" disabled>edit</button>'
        }
        else
		    var buttonHtml = ' <button onclick="displayEditBrand(' + e.id + ')">edit</button>';
		var brand = (e.brand.length>maxLength)?e.brand.substring(0,maxLength)+'...':e.brand;
		var category = (e.category.length>maxLength)?e.category.substring(0,maxLength)+'...':e.category;
		table.row.add([
          brand,
          category,
          buttonHtml
        ]).draw();
	}
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
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-brand-modal').modal('toggle');
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
    }
    document.getElementById("download-errors").disabled = true;
    table = $('#brand-table').DataTable({'columnDefs': [ {'targets': [2],'orderable': false }]});
}
$(document).ready(getBrandList);
$(document).ready(init);


