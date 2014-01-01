
INSERT INTO `companies` (`id`, `name`, `active`) VALUES
(1, 'Milestone Intelligence Group, Inc.', true);


INSERT INTO `users` (`id`, `company_id`, `login`, `hashed_pass`, `email`, `first_name`, `last_name`, `active`) VALUES
(1, 'mday', 'cf099c00087b3c7d3d34f5858b170f9cab94806d78aa04414cfd429956feb8abf1c3cc42125ccd0c03f9b3a14118397a878ae97bfd50b5478ede5098de71fd92', 'mday@arcblaze.com', 'Mike', 'Day', 1),
(2, 'user', '6cc3cface1495039e30b64f1319ba9daf12e31dd6440637d6c7aaae07e5d61051097c213dae4923a3da8a7bc236fa93cbc1b1c6f005ad08d5bf787166eda68f6', 'user@arcblaze.com', 'User', 'Last', 1);


INSERT INTO `roles` (`name`, `user_id`) VALUES
('ADMIN', 1);

