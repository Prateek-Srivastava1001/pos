var table;
function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}
function getAdminProductUrl(){
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/admin/product";
}
//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update
	var $form = $("#product-form");
	var formData = $form.serializeArray();
    	var barcode = formData[0].value;
    	var brand = formData[1].value;
    	var category = formData[2].value;
    	var name = formData[3].value;
    	var mrp = formData[4].value;
    	if(barcode==null ||barcode==""){
            warnClick("Barcode cannot be empty");
            return true;
        }
    	if(brand==null ||brand==""){
            warnClick("Brand cannot be empty");
            return true;
        }
        if(category==null ||category==""){
            warnClick("Category cannot be empty");
            return true;
        }
        if(name==null ||name==""){
            warnClick("MRP cannot be empty");
            return true;
        }
       if(mrp==null ||mrp==""){
            warnClick("MRP cannot be empty");
            return true;
        }
	var json = toJson($form);
	console.log(json);
	var url = getAdminProductUrl();

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

function updateProduct(event){
	$('#edit-product-modal').modal('toggle');
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();
	var url = getAdminProductUrl() + "/" + id;

	//Set the values to update
	var $form = $("#product-edit-form");
	var formData = $form.serializeArray();
    var barcode = formData[0].value;
    var brand = formData[1].value;
    var category = formData[2].value;
    var name = formData[3].value;
    var mrp = formData[4].value;
    console.log(mrp);
    if(barcode==null ||barcode==""){
           dangerClick("Barcode cannot be empty");
           return true;
       }
    if(brand==null ||brand==""){
           dangerClick("Brand cannot be empty");
           return true;
       }
       if(category==null ||category==""){
           dangerClick("Category cannot be empty");
           return true;
       }
       if(name==null ||name==""){
           dangerClick("MRP cannot be empty");
           return true;
       }
      if(mrp==null ||mrp==""){
           dangerClick("MRP cannot be empty");
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
	   		getProductList();
	   },
	   error: handleAjaxError
	});

	return false;
}


function getProductList(){
	var url = getProductUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProductList(data);
	   },
	   error: handleAjaxError
	});
}


// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#productFile')[0].files[0];
    //Checking for .tsv extension
	var extension = getExtension($('#productFile').val());
	console.log(extension);
    if(extension.toLowerCase() != 'tsv'){
    dangerClick('Please Upload File with extension .tsv only...');
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
    	    warnClick("There were some issues with the uploaded files, please download errors to find more");
    	}
    	else{
    	successClick("File upload successful");
    	}
    	getProductList();
		return;
	}

	//Process next row
	var row = fileData[processCount];
	processCount++;

	var json = JSON.stringify(row);
	var url = getAdminProductUrl();
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

function displayProductList(data){
	var $tbody = $('#product-table').find('tbody');
	table.clear().draw();
	for(var i in data){
		var e = data[i];
		var roleElement = document.getElementById('role');
        var role = roleElement.innerText;
        if(role=="operator"){
            var buttonHtml = ' <button class="edit_btn" disabled>edit</button>'
        }
        else
		    var buttonHtml = ' <button onclick="displayEditProduct(' + e.id + ')">edit</button>';
        table.row.add([
                      e.barcode,
                      e.brand,
                      e.category,
                      e.name,
                      e.mrp,
                      buttonHtml
                     ]).draw();
	}
}

function displayEditProduct(id){
	var url = getProductUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data);
	   },
	   error: handleAjaxError
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
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
	var $file = $('#productFile');
	var fileName = $file.val().replace(/.*(\/|\\)/, '');
	$('#productFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-product-modal').modal('toggle');
}

function displayProduct(data){
	$("#product-edit-form input[name=barcode]").val(data.barcode);
	$("#product-edit-form input[name=brand]").val(data.brand);
	$("#product-edit-form input[name=category]").val(data.category);
	$("#product-edit-form input[name=name]").val(data.name);
	$("#product-edit-form input[name=mrp]").val(data.mrp);
	$("#product-edit-form input[name=id]").val(data.id);
	$('#edit-product-modal').modal('toggle');
}

function refresh(){
    location.reload(true);
}
//INITIALIZATION CODE
function init(){
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName);

    var roleElement = document.getElementById('role');
    var role = roleElement.innerText;

    if(role=="operator"){
        document.getElementById("add-product").disabled = true;
        document.getElementById("update-product").disabled = true;
        document.getElementById("upload-data").disabled = true;
        document.getElementById("process-data").disabled = true;
    }
    document.getElementById("download-errors").disabled = true;
    table = $('#product-table').DataTable({'columnDefs': [ {'targets': [5],'orderable': false }]});
}

$(document).ready(init);
$(document).ready(getProductList);

