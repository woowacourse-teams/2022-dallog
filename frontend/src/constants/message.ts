const CONFIRM_MESSAGE = {
  DELETE: '정말 삭제하시겠습니까?',
  UNSUBSCRIBE: '구독을 해제하시겠습니까?',
  LOGOUT: '로그아웃하시겠습니까?',
};

const ERROR_MESSAGE = {
  DEFAULT: '에러가 발생했습니다. 잠시 후에 다시 시도해주세요.',
};

const VALIDATION_MESSAGE = {
  STRING_LENGTH: (min: number, max: number) => `${min}자 ~ ${max}자로 입력해주세요.`,
};

export { CONFIRM_MESSAGE, ERROR_MESSAGE, VALIDATION_MESSAGE };
