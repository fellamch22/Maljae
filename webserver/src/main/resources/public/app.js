let preference = document.querySelector("#preferences");
let teamName = document.getElementById("teamName");
let memberGrid = document.getElementById("memberGrid");
let form = document.querySelector("form");
let submit = document.getElementById("submit");
let choices = document.getElementsByClassName("choices");
let removed = document.querySelectorAll(".remove");
//let preferences = document.getElementById("preferences");
//console.log(preferences);
let emails = [];
$.getJSON("../../../../../datamodel/src/test/resources/config.json", data => {
  data.tasks.forEach(task => {
    let choice = document.createElement("button");
    choice.innerHTML = `<span id="task">${task.identifier}</span><div class="description">
    <span class="choiceTitle">${task.title}</span><br/> <p>${task.description}</p></div>`;
    choice.className = "choices";
    choice.value = task.identifier;
    choice.addEventListener("click", () => {
      console.log("clicked");
      choice.className = choice.className.includes(" valide")
        ? "choices"
        : "choices valide";
    });
    choice.addEventListener("mouseover", () => {
      preference.style.columnGap = "1.8em";
      preference.style.rowGap = "2em";
    });
    choice.addEventListener("mouseleave", () => {
      preference.style.columnGap = ".2em";
      preference.style.rowGap = ".2em";
      console.log(preference);
    });
    preference.appendChild(choice);
  });
});

$.getJSON("../../../../maljae-data/A1-team.json", data => {
  dataSaved = data;
  teamName.textContent += " " + data.identifier;
  data.students.forEach(student => {
    console.log(student.email);
    let newelement = document.createElement("div");
    newelement.innerHTML = `<button class="btn_email">${student.email}</button><button class="remove"> X </button>`;
    newelement.childNodes[1].addEventListener("click", () => {
      newelement.parentNode.removeChild(newelement);
      emails.removed(student.email);
    });
    emails.push(student.email);
    newelement.className = "emails";
    console.log(newelement);
    memberGrid.appendChild(newelement);
  });
  data.preferences.forEach(element => {
    for (let i = 0; i < choices.length; i++) {
      console.log(choices[i].value.includes(element));
      choices[i].className += choices[i].value.includes(element)
        ? " valide"
        : "";
    }
  });
  data.preferences.push("task3");
  console.log(JSON.stringify($(form).serializeArray()));
});
function ValidateEmail(mail) {
  if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(mail)) {
    return true;
  }
  alert("You have entered an invalid email address!");
  return false;
}

const emailInput = document.querySelector("#email_input");
emailInput.addEventListener("keypress", e => {
  if (
    e.keyCode == 13 &&
    ValidateEmail(emailInput.value) &&
    !emails.includes(emailInput.value)
  ) {
    let newelement = document.createElement("div");
    newelement.innerHTML = `<button class="btn_email">${emailInput.value}</button><button class="remove"> X </button>`;
    emails.push(emailInput.value);
    console.log(emails);
    newelement.childNodes[1].addEventListener("click", () => {
      newelement.parentNode.removeChild(newelement);
      emails.removed(emailInput.value);
    });
    newelement.className = "emails";
    memberGrid.appendChild(newelement);
    emailInput.value = "";
  }
});
console.log(removed[0]);
