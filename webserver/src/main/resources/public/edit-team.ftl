<html>
  <head>
    <meta charset="utf-8" />
    <meta content="IE=edge" http-equiv="X-UA-Compatible" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <style type="text/css">
      <#include "./editTeam.css">
    </style>
    <script src="app.js"></script>
    <title>maljae home</title>
  </head>

  <body>
    <div class="container">
      <div class="title">
        <h2 id="teamName">Information about ${teamName}</h2>
      </div>
      <form action="/team/update/${token}" method="post">
        <label for="email">Enter your secret:<br /> </label>
        <input
          class="input"
          type="string"
          name="secret"
          id="secret"
          value="${secret}"
          required
        />
        <label for="memberGrid"><br />Enter the members of your team</label>
        <input
          id="email_input"
          type="email"
          class="input"
          placeholder="enter new email"
        />
        <button class="input ajouter" type="button">Ajouter</button>
        <div id="memberGrid"></div>
        <label for="preferences">Team Preferences : </label>
        <div id="preferences"></div>
        <textarea required name="students" class="invisible emailsInv">
${students}</textarea
        >
        <textarea required name="preferences" class="invisible tasksInv">
${preferences}</textarea
        >
        <table>
          <tr>
            <th style="padding-right: 10px;">Monday</th>
            <th style="padding-right: 10px;">Tuesday</th>
            <th style="padding-right: 10px;">Wednesday</th>
            <th style="padding-right: 10px;">Thursday</th>
            <th style="padding-right: 10px;">Friday</th>
            <th style="padding-right: 10px;">Saturday</th>
            <th style="padding-right: 10px;">Sunday</th>
          </tr>
          <tr>
            <th>AM PM</th>
            <th>AM PM</th>
            <th>AM PM</th>
            <th>AM PM</th>
            <th>AM PM</th>
            <th>AM PM</th>
            <th>AM PM</th>
          </tr>
          <tr>
            <th>
              <input type="checkbox" id="lundiam" name="lundiAM" ${edt0} />
              <input type="checkbox" id="lundipm" name="lundiPM" ${edt1} />
            </th>
            <th>
              <input type="checkbox" id="mardiam" name="mardiAM" ${edt2} />
              <input type="checkbox" id="mardipm" name="mardiPM" ${edt3} />
            </th>
            <th>
              <input
                type="checkbox"
                id="mercrediam"
                name="mercrediAM"
                ${edt4}
              />
              <input
                type="checkbox"
                id="mercredipm"
                name="mercrediPM"
                ${edt5}
              />
            </th>
            <th>
              <input type="checkbox" id="jeudiam" name="jeudiAM" ${edt6} />
              <input type="checkbox" id="jeudipm" name="jeudiPM" ${edt7} />
            </th>
            <th>
              <input
                type="checkbox"
                id="vendrediam"
                name="vendrediAM"
                ${edt8}
              />
              <input
                type="checkbox"
                id="vendredipm"
                name="vendrediPM"
                ${edt9}
              />
            </th>
            <th>
              <input type="checkbox" id="samediam" name="samediAM" ${edt10} />
              <input type="checkbox" id="samedipm" name="samediPM" ${edt11} />
            </th>
            <th>
              <input
                type="checkbox"
                id="dimancheam"
                name="dimancheAM"
                ${edt12}
              />
              <input
                type="checkbox"
                id="dimanchepm"
                name="dimanchePM"
                ${edt13}
              />
            </th>
          </tr>
        </table>

        <button class="input" id="submit" type="submit">
          update
        </button>
      </form>
      <ul class="bg-bubbles">
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
      </ul>
    </div>
   <script >
   let preference = document.querySelector("#preferences");
let teamName = document.getElementById("teamName");
let memberGrid = document.getElementById("memberGrid");
let form = document.querySelector("form");
let submit = document.getElementById("submit");
let choices = document.getElementsByClassName("choices");
let removed = document.querySelectorAll(".remove");
let emailsInv = document.querySelector(".emailsInv");
let tasksInv = document.querySelector(".TasksInv");
let emails = emailsInv.value.split(";");
let tasks = tasksInv.value.split(";");
const ajouterKey = document.querySelector(".ajouter");

console.log(emailsInv.value);
console.log(emails);
tasks.forEach(task => {
if(task!=""){
  let choice = document.createElement("button");
  choice.type = "button";
  choice.innerHTML =
    '<span id="task">' + (tasks.indexOf(task) + 1) + " :" + task + "</span>";
  choice.className = "choices valide";
  choice.value = task;
  choice.addEventListener("click", e => {
    console.log("clicked");
    if (choice.className.includes(" valide")) {
      tasks.splice(tasks.indexOf(choice.value), 1);
      choice.innerHTML = '<span id="task">' + task + "</span>";
      choice.className = "choices";
      tasksInv.value = tasks.join(";");
      for (let i = 0; i < tasks.length; i++) {
        if (preferences.childNodes[i].className.includes(" valide"))
          preferences.childNodes[i].innerHTML =
            '<span id="task">' +
            (tasks.indexOf(preferences.childNodes[i].value) + 1) +
            " : " +
            preferences.childNodes[i].value +
            "</span>";
      }
    } else {
      tasks.push(choice.value);
      choice.className = "choices valide";
      choice.innerHTML =
        '<span id="task">' +
        (tasks.indexOf(task) + 1) +
        " : " +
        task +
        "</span>";
      tasksInv.value = tasks.join(";");
      for (let i = 0; i < tasks.length; i++) {
        if (preferences.childNodes[i].className.includes(" valide"))
          preferences.childNodes[i].innerHTML =
            '<span id="task">' +
            (tasks.indexOf(preferences.childNodes[i].value) + 1) +
            " : " +
            preferences.childNodes[i].value +
            "</span>";
      }
    }
  });
  preference.appendChild(choice);
  }
});

emails.forEach(student => {
  if (student != "") {
    const email = student.split("/")[0];
    console.log(email);
    if (ValidateEmail(email)) {
      let newelement = document.createElement("div");
      newelement.innerHTML =
        '<button class="btn_email" type="button">' +
        email +
        '</button><span class="remove"> X </span>';
      newelement.childNodes[1].addEventListener("click", e => {
        if (e.keyCode != 13) {
          console.log(e);
          const index = emails.indexOf(student);
          emails.splice(index, 1);
          emailsInv.value = emails.join(";");
          newelement.parentNode.removeChild(newelement);
        }
      });

      newelement.className = "emails";
      console.log(newelement);
      memberGrid.appendChild(newelement);
    }
  }
});


function ValidateEmail(mail) {
  if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(mail)) {
    return true;
  }
  return false;
}

let emailInput = document.querySelector("#email_input");
console.log(emailInput);
ajouterKey.addEventListener("click", e => {
  if (ValidateEmail(emailInput.value) && !emails.includes(emailInput.value)) {
    emails.push(emailInput.value);
    let newelement = document.createElement("div");
    newelement.innerHTML =
      '<button class="btn_email" type="button">' +
      emailInput.value +
      '</button><span class="remove"> X </span>';
    console.log(newelement.childNodes[1]);
    newelement.childNodes[1].addEventListener("click", e => {
      if (e.keyCode != 13) {
        console.log(e);
        const index = emails.indexOf(emailInput.value);
        emails.splice(index, 1);
        emailsInv.value = emails.join(";");
        newelement.parentNode.removeChild(newelement);
      }
    });
    emailsInv.value = emails.join(";");
    newelement.className = "emails";
    console.log(newelement);
    memberGrid.appendChild(newelement);
  }
  emailInput.value = "";
});

</script>

    

    
  </body>
</html>
