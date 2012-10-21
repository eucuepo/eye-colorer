<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title>Change your eye color!</title>
<script type="text/javascript" src="script/jquery.js"></script>
<script type="text/javascript" src="script/jscolor.js"></script>
<script type="text/javascript" src="script/ajaxuploader.js"></script>
<script type="text/javascript">
	//prepare the form when the DOM is ready 
	var ajaxUpload;

	$(document).ready(function() {

		var thumb = $('img#thumb');

		ajaxUpload = new AjaxUpload('imageUpload', {
			action : $('form#newHotnessForm').attr('action'),
			name : 'image',
			data : {
				color : 'FFFFFF'
			},
			autoSubmit : false,
			onSubmit : function(file, extension) {
				$('div.preview').addClass('loading');
			},
			onComplete : function(file, response) {
				thumb.load(function() {
					$('div.preview').removeClass('loading');
					thumb.unbind();
				});
				thumb.attr('src', response);
			}
		});
	});
</script>
</head>

<body>

	<form action="UploadImage" method="post" enctype="multipart/form-data"
		id="newHotnessForm" onsubmit="return false;">
		<input type="file" size="20" id="imageUpload" class=" " name="image" />
		<input id="color" class="color" name="color"
			onchange="ajaxUpload.data.color=this.color" />
		<button type="submit" onclick="ajaxUpload.submit();" class="button">Save</button>
	</form>
	<div class="preview">
		<img id="thumb" width="600px" src="script/hs.png" />
	</div>
</body>

</html>