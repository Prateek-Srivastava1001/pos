var table;
var brandCategoryData = [];
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
            warnClick("Name cannot be empty");
            return true;
        }
       if(mrp==null ||mrp==""){
            warnClick("MRP cannot be empty");
            return true;
        }
        if(parseFloat(mrp)<0){
            warnClick("MRP cannot be negative");
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
	        successClick("Data added successfully");
            $("#product-form input[name=barcode]").val("");
            $("#product-form input[name=brand]").val("");
            $("#product-form input[name=category]").val("");
            $("#product-form input[name=name]").val("");
            $("#product-form input[name=mrp]").val("");
	   		getProductList();
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
       if(parseFloat(mrp)<0){
        warnClick("MRP cannot be negative");
        return;
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
		    var buttonHtml = ' <button class="btn btn-outline-info" onclick="displayEditProduct(' + e.id + ')">edit</button>';
        table.row.add([
                      e.barcode,
                      e.brand,
                      e.category,
                      e.name,
                      'Rs '+(Math.round(parseFloat(e.mrp) * 100) / 100).toFixed(2),
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
    var brandDropdown = $('#editBrand');
    populateBrandDropdown(brandDropdown);
    $("#editBrand").val(data.brand).trigger("change");
    $("#editCategory").val(data.category).trigger("change");

	$("#product-edit-form input[name=barcode]").val(data.barcode);
	$("#product-edit-form input[name=name]").val(data.name);
	$("#product-edit-form input[name=mrp]").val(data.mrp);
	$("#product-edit-form input[name=id]").val(data.id);
	$('#edit-product-modal').modal('toggle');
}

function refresh(){
    location.reload(true);
}
//Brand - Category combination for dropdown
function getBrandList(){
    var url = $("meta[name=baseUrl]").attr("content")+'/api/brand';
    $.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {
    	   		brandCategoryData = data;
    	   		var brandDropdown = $('#inputBrand');
    	   		populateBrandDropdown(brandDropdown);
    	   },
    	   error: handleAjaxError
    	});
}
function populateBrandDropdown(brandDropdown){
    // Clear existing options
    brandDropdown.empty();
    // Add an empty option
    brandDropdown.append($('<option>', {
        value: '',
        text: 'Select'
    }));
    var distinctBrands = new Set();
    // Iterate over the brand-category data to collect distinct brands
    brandCategoryData.forEach(function(item) {
       distinctBrands.add(item.brand);
    });

    distinctBrands.forEach(function(brand) {
            brandDropdown.append($('<option>', {
                value: brand,
                text: brand
            }));
        });
}
function populateCategoryDropdown(selectedBrand, categoryDropdown){
    categoryDropdown.empty();
    categoryDropdown.append($('<option>', {
            value: '',
            text: 'Select'
        }));
    // Filter the brand-category data based on the selected brand
    var filteredData = brandCategoryData.filter(function(item) {
        return item.brand === selectedBrand;
    });

    filteredData.forEach(function(item) {
            categoryDropdown.append($('<option>', {
                value: item.category,
                text: item.category
            }));
        });
}
//INITIALIZATION CODE
function init(){
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName);
    $('#inputBrand').on('change', function() {
                                               var selectedBrand = $(this).val();
                                               var categoryDropdown = $('#inputCategory');
                                               populateCategoryDropdown(selectedBrand, categoryDropdown);
                                            });
    $('#editBrand').on('change', function() {
                                                var selectedBrand = $(this).val();
                                                var categoryDropdown = $('#editCategory');
                                                populateCategoryDropdown(selectedBrand, categoryDropdown);
                                                });

    var roleElement = document.getElementById('role');
    var role = roleElement.innerText;

    if(role=="operator"){
        document.getElementById("add-product").disabled = true;
        document.getElementById("update-product").disabled = true;
        document.getElementById("upload-data").disabled = true;
        document.getElementById("process-data").disabled = true;
        document.getElementById("product-form").innerHTML = "";
        document.getElementById("edit-product-modal").innerHTML = "";
    }
    document.getElementById("download-errors").disabled = true;
    table = $('#product-table').DataTable({'columnDefs': [ {'targets': [5],'orderable': false },
                {'targets': [0,1,2,3,4,5], "className": "text-center"}],
             searching: false,
             info:false,
             lengthMenu: [
                     [10, 25, 50, -1],
                     [10, 25, 50, 'All']
                 ]
    });
    $('.select2').select2();

}

$(document).ready(init);
$(document).ready(getProductList);
$(document).ready(getBrandList);

