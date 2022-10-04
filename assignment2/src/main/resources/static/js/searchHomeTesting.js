

const image_input = document.querySelector("#image-input");
let uploadForm = document.getElementById("uploadForm");
image_input.addEventListener("change", function() {
  const reader = new FileReader();
  reader.addEventListener("load", () => {
    const uploaded_image = reader.result;
//    document.querySelector("#display-image").style.backgroundImage = `url(${uploaded_image})`;
    uploadForm.submit();
  });
reader.readAsDataURL(this.files[0]);

});

