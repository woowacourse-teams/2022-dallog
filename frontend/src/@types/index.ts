interface InputRef {
  [index: string]: React.RefObject<HTMLInputElement>;
}

interface Schedule {
  id: number;
  title: string;
  startDateTime: string;
  endDateTime: string;
  memo: string;
}

export { InputRef, Schedule };
