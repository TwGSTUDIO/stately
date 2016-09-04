/**
 * Copyright 2016 Lloyd Torres
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lloydtorres.stately.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lloyd on 2016-01-28.
 * An object containing all information about one issue encountered in NationStates.
 */
public class Issue implements Parcelable {
    public static final String QUERY = "https://www.nationstates.net/page=dilemmas/template-overall=none";

    public int id;
    public String chain;
    public String title;
    public String content;
    public List<IssueOption> options;

    public Issue() { super(); }

    protected Issue(Parcel in) {
        id = in.readInt();
        chain = in.readString();
        title = in.readString();
        content = in.readString();
        if (in.readByte() == 0x01) {
            options = new ArrayList<IssueOption>();
            in.readList(options, IssueOption.class.getClassLoader());
        } else {
            options = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(chain);
        dest.writeString(title);
        dest.writeString(content);
        if (options == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(options);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Issue> CREATOR = new Parcelable.Creator<Issue>() {
        @Override
        public Issue createFromParcel(Parcel in) {
            return new Issue(in);
        }

        @Override
        public Issue[] newArray(int size) {
            return new Issue[size];
        }
    };
}