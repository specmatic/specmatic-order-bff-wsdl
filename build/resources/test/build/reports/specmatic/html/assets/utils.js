const extractValuesFromTableRow = (tableRow) => {
  let firstGroup;
  let secondGroup;
  let thirdGroup;
  let exercised;

  const values = [...tableRow.children].map((child) => child.textContent);

  if (values.length === 6) {
    firstGroup = values[1].trim();
    secondGroup = values[2].trim();
    thirdGroup = values[3].trim();
    exercised = Number(values[4]);
  } else {
    firstGroup = values[0].trim();
    secondGroup = values[1].trim();
    thirdGroup = values[2].trim();
    exercised = Number(values[3]);
  }

  return {
      coverage: Number(tableRow.getAttribute("data-coverage")),
      firstGroup: firstGroup,
      secondGroup: secondGroup,
      thirdGroup: thirdGroup,
      exercised: exercised,
      result: tableRow.getAttribute("data-result"),
      color: tableRow.getAttribute("data-color"),
      type: tableRow.getAttribute("data-type"),
  };
};

const goBackToTable = (scrollBack = true) => {
  setTimeout(() => {
    scenariosList.replaceChildren();
  }, 300);
  mainElement.setAttribute("data-item", "table");
  if(headerSummary.getAttribute("data-panel") === "details") {
    resetSummaryHeader();
    headerSummary.setAttribute("data-panel", "table");
  }
  if (scrollBack) {
    scrollTo(0, scrollYPosition);
  }
};

const calculateTestGroups = (scenarios) => {
  const groups = scenarios.reduce(
    (acc, scenario) => {
      acc[scenario.htmlResult] += 1;
      acc.Total += 1
      return acc;
    },
    { Success: 0, Failed: 0, Error: 0, Skipped: 0, Total: 0 }
  );
  return groups;
};