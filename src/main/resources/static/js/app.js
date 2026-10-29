document.addEventListener("DOMContentLoaded", () => {
  const roleRadios = document.querySelectorAll('input[name="role"]');
  const seniorBox = document.getElementById("seniorRequirements");
  const careerInput = document.getElementById("careerSummary");
  if (!roleRadios.length || !seniorBox || !careerInput) return;

  const syncRole = () => {
    const selected = document.querySelector('input[name="role"]:checked');
    const isSenior = selected && selected.value === "SENIOR";
    seniorBox.hidden = !isSenior;
    careerInput.required = isSenior;
  };
  roleRadios.forEach((radio) => radio.addEventListener("change", syncRole));
  syncRole();
});
