const CONFIRM_MESSAGE = {
  DELETE: '정말 삭제하시겠습니까?',
  UNSUBSCRIBE: '구독을 해제하시겠습니까?',
  LOGOUT: '로그아웃하시겠습니까?',
};

const VALIDATION_MESSAGE = {
  STRING_LENGTH: (min: number, max: number) => `${min}자 ~ ${max}자로 입력해주세요.`,
};

export { CONFIRM_MESSAGE, VALIDATION_MESSAGE };
