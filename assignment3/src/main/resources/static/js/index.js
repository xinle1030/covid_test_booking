function scrollToSearchResult(){
    let searchContent = document.getElementById("searchContent");
    searchContent.scrollIntoView({
        behaviour: "smooth",
        block: "start",
        inline: "nearest"
    });
}


