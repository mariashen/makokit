# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20160407232739) do

  create_table "answer_jumps", force: :cascade do |t|
    t.integer  "answer_id"
    t.integer  "instruction_id"
    t.datetime "created_at",     null: false
    t.datetime "updated_at",     null: false
  end

  create_table "answers", force: :cascade do |t|
    t.integer  "instruction_id"
    t.string   "text"
    t.string   "image_url"
    t.datetime "created_at",     null: false
    t.datetime "updated_at",     null: false
    t.boolean  "correct"
  end

  create_table "instructions", force: :cascade do |t|
    t.integer  "lesson_id"
    t.text     "text"
    t.string   "image_url"
    t.string   "video_url"
    t.datetime "created_at",          null: false
    t.datetime "updated_at",          null: false
    t.integer  "next_instruction_id"
    t.integer  "display_index"
  end

  create_table "lessons", force: :cascade do |t|
    t.datetime "created_at",  null: false
    t.datetime "updated_at",  null: false
    t.string   "name"
    t.integer  "version"
    t.string   "category"
    t.text     "description"
  end

end
