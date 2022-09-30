// Validation errors messages for Parsley
// Load this after Parsley

Parsley.addMessages('uz', {
  defaultMessage: "Qiymat noto‘g‘ri",
  type: {
    email: "Elektron pochta manzilingizni kiriting",
    url: "URL manzilini kiriting",
    number: "Raqamni kiriting",
    integer: "Butun sonni kiriting",
    digits: "Faqat raqamlarni kiriting",
    alphanum: "Harf-raqamli qiymatni kiriting"
  },
  notblank: "Bu maydonni to‘ldirish kerak",
  required: "Majburiy maydon",
  pattern: "Bu qiymat yaroqsiz",
  min: "Bu qiymat kamida %s bo‘lishi kerak",
  max: "Bu qiymat %s dan oshmasligi kerak",
  range: "Bu qiymat %s dan %s gacha bo‘lishi kerak",
  minlength: "Bu qiymat kamida %s belgidan iborat bo‘lishi kerak",
  maxlength: "Bu qiymat eng ko‘p %s belgidan iborat bo‘lishi kerak",
  length: "Bu qiymat %s dan %s gacha bo‘lishi kerak",
  mincheck: "Kamida %s qiymatini tanlang",
  maxcheck: "Eng ko‘p %s qiymatini tanlang",
  check: "%s dan %s gacha qiymatlarni tanlang",
  equalto: "Bu qiymat mos kelishi kerak"
});

Parsley.setLocale('uz');
