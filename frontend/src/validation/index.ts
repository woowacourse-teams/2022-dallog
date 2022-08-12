const validateLength = (target: string, min: number, max: number) => {
  return min <= target.length && target.length <= max;
};

const validateNotEmpty = (target: string) => {
  return target.length > 0;
};

const validateStartEndDateTime = (startDate: string, endDate: string) => {
  return startDate <= endDate;
};

export { validateLength, validateNotEmpty, validateStartEndDateTime };
