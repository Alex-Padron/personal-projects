(require 'package)

;; Add melpa package source when using package list
(add-to-list 'package-archives '("melpa" . "http://melpa.org/packages/") t)

;; Load emacs packages and activate them
;; This must come before configurations of installed packages.
;; Don't delete this line.
(package-initialize)
(custom-set-variables
 ;; custom-set-variables was added by Custom.
 ;; If you edit it by hand, you could mess it up, so be careful.
 ;; Your init file should contain only one such instance.
 ;; If there is more than one, they won't work right.
 '(coffee-tab-width 2)
 '(custom-safe-themes (quote ("0c29db826418061b40564e3351194a3d4a125d182c6ee5178c237a7364f0ff12" "58c6711a3b568437bab07a30385d34aacf64156cc5137ea20e799984f4227265" "72a81c54c97b9e5efcc3ea214382615649ebb539cb4f2fe3a46cd12af72c7607" "9b59e147dbbde5e638ea1cde5ec0a358d5f269d27bd2b893a0947c4a867e14c1" "a8245b7cc985a0610d71f9852e9f2767ad1b852c2bdea6f4aadc12cce9c4d6d0" "d677ef584c6dfc0697901a44b885cc18e206f05114c8a3b7fde674fce6180879" "8aebf25556399b58091e533e455dd50a6a9cba958cc4ebb0aab175863c25b9a4" default)))
 '(inhibit-startup-screen t)
 '(js-indent-level 2))
(custom-set-faces
 ;; custom-set-faces was added by Custom.
 ;; If you edit it by hand, you could mess it up, so be careful.
 ;; Your init file should contain only one such instance.
 ;; If there is more than one, they won't work right.
 '(default ((t (:background nil)))))

;; add line numbers
(global-linum-mode 1)

;; make go-mode tabs 4 spaces instead of 8
(add-hook 'go-mode-hook
          (lambda ()
            (add-hook 'before-save-hook 'gofmt-before-save)
            (setq tab-width 4)
            (setq indent-tabs-mode 1)))

;; remove the toolbar on top of the gui
(tool-bar-mode -1)

;; limit char counts in a line
(require 'whitespace)
(setq whitespace-style '(face empty trailing lines-tail))
(global-whitespace-mode t)

;; disable mouse clicks in the gui
(define-minor-mode disable-mouse-mode
  "A minor-mode that disables all mouse keybinds."
  :global t
  :lighter " 🐭"
  :keymap (make-sparse-keymap))

(dolist (type '(mouse down-mouse drag-mouse
                      double-mouse triple-mouse))
  (dolist (prefix '("" C- M- S- M-S- C-M- C-S- C-M-S-))
    (dotimes (n 7)
      (let ((k (format "%s%s-%s" prefix type n)))
        (define-key disable-mouse-mode-map
          (vector (intern k)) #'ignore)))))
(disable-mouse-mode 1)

;; load the graham color scheme
(if (daemonp)
    (add-hook 'after-make-frame-functions
        (lambda (frame)
            (select-frame frame)
            (load-theme 'spolsky t)))
    (load-theme 'spolsky t))

;; set custom key bindings
(global-set-key (kbd "C-o") 'other-window)

(global-set-key (kbd "C-d") 'backward-char)

(global-set-key (kbd "C-r") 'revert-buffer)

(global-unset-key (kbd "C-j"))

(add-hook 'term-mode-hook (lambda() (term-set-escape-char ?\C-j)))

(global-set-key (kbd "C-j l") 'term-char-mode)

(global-set-key (kbd "C-j j") 'term-line-mode)

(load "~/.emacs.d/sublime-text-2.el") 
