const validateLength = (target: string, min: number, max: number) => {
  return min <= target.length && target.length <= max;
};

const validateStartEndDate = (startDate: string, endDate: string) => {
  return startDate <= endDate;
};

export { validateLength, validateStartEndDate };
