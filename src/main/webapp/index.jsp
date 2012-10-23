<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title>Change your eye colors!</title>
<link rel="stylesheet" type="text/css" href="css/simple.css">
<script type="text/javascript" src="script/jquery.js"></script>
<script type="text/javascript" src="script/jscolor.js"></script>
<script type="text/javascript" src="script/ajaxuploader.js"></script>
<script type="text/javascript">
	//prepare the form when the DOM is ready 
	var eyeColor = 'FFFFFF';
	var multifaceChecked = false;

	var ajaxUpload;

	$(document).ready(function() {

		var thumb = $('img#thumb');

		ajaxUpload = new AjaxUpload('imageUpload', {
			action : $('form#newHotnessForm').attr('action'),
			name : 'image',
			data : {
				color : 'FFFFFF',
				multiface : false
			},
			autoSubmit : false,
			onSubmit : function(file, extension) {
				$('div.loader').addClass('loading');
			},
			onComplete : function(file, response) {
				thumb.load(function() {
					$('div.loader').removeClass('loading');
					thumb.unbind();
				});
				thumb.attr('src', response);
			}
		});
	});

	function changeMultiface() {
		multifaceChecked = $('#multiface').attr('checked') ? true : false;
		setData();
	}

	function changeColor(chosenColor) {
		eyeColor = chosenColor;
		setData();
	}

	function setData() {
		ajaxUpload.setData({
			color : eyeColor,
			multiface : multifaceChecked
		});
	}
</script>
</head>

<body>
	<div class="content">
		<div class="border">
			<h2>Change your eye colors!</h2>
		</div>
		<div class="wrapper">
			<div class="text  border">
				<p>Welcome to the eye colorer! Just choose an image from your hard disk, choose the color, and you are good to go!</p>
				<p>To get better results:</p>
				<ul>
					<li>Choose an image with good resolution (+1024px)</li>
					<li>Your face should fill the image as much as possible</li>
					<li>Make sure the image has good ilumination, otherwise the app would have a hard time finding your eyes!</li>
				</ul>
			</div>
			<div class="center">
				<div class="preview">
					<img id="thumb" width="600px" src="img/silhouette.png" />
				</div>
				<div class="buttons border">
					<form action="UploadImage" method="post" enctype="multipart/form-data" id="newHotnessForm" onsubmit="return false;">
						<input type="file" size="20" id="imageUpload" class=" " name="image" /> 
						<input id="color" class="color" name="color" onchange="changeColor(this.color);" />
						<!-- <p>Enable multiface</p><input type="checkbox" name="multiface" id="multiface" onchange="changeMultiface()" /> -->
						<button type="submit" onclick="ajaxUpload.submit();" class="button">Save</button>
						<div class="loader"></div>
					</form>
				</div>
			</div>
			<div class="border signature">
				Developed by Eugenio Cuevas (eucuepo at gmail) for a <a href="http://cloudspokes.com">Cloudspokes</a> challenge
			</div>
		</div>
	</div>
</body>

</html>