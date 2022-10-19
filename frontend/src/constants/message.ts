const CONFIRM_MESSAGE = {
  DELETE: '정말 삭제하시겠습니까?',
  UNSUBSCRIBE: '구독을 해제하시겠습니까?',
  LOGOUT: '로그아웃하시겠습니까?',
  WITHDRAWAL: '정말 탈퇴하시겠습니까?',
  ADD_ADMIN: '카테고리 편집 권한을 부여하시겠습니까?',
  DELETE_ADMIN: '카테고리 편집 권한을 삭제하시겠습니까?',
  FORGIVE_ADMIN: '정말 카테고리 편집 권한을 포기하시겠습니까?',
};

const ERROR_MESSAGE = {
  DEFAULT: '에러가 발생했습니다. 잠시 후에 다시 시도해주세요.',
};

const SUCCESS_MESSAGE = {
  DELETE_CATEGORY: '카테고리를 삭제했습니다.',
  DELETE_PROFILE: '회원 탈퇴되었습니다.',
  DELETE_SCHEDULE: '일정을 삭제했습니다.',
  PATCH_CATEGORY_NAME: '카테고리 이름이 변경되었습니다.',
  PATCH_CATEGORY_ROLE: '편집 권한을 변경하였습니다.',
  PATCH_PROFILE_NAME: '이름을 변경하였습니다.',
  PATCH_SCHEDULE: '일정을 변경하였습니다.',

  POST_CATEGORY: '카테고리를 생성하였습니다.',
  POST_LOGIN: '로그인에 성공했습니다.',
  POST_SCHEDULE: '일정을 생성했습니다.',
};

const TOOLTIP_MESSAGE = {
  CANNOT_UNSUBSCRIBE_EDITABLE_CATEGORY: '편집 권한이 있는 카테고리는 구독을 해제할 수 없습니다.',
  CANNOT_EDIT_DELETE_DEFAULT_CATEGORY: '기본 카테고리는 수정/삭제가 불가능합니다.',
};

export { CONFIRM_MESSAGE, ERROR_MESSAGE, SUCCESS_MESSAGE, TOOLTIP_MESSAGE };
